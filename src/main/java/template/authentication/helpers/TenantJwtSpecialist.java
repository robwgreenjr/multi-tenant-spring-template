package template.authentication.helpers;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import template.authentication.constants.AuthenticationVariable;
import template.authentication.exceptions.InvalidJwtException;
import template.global.exceptions.KnownServerException;
import template.global.exceptions.UnknownServerException;
import template.global.models.ConfigurationModel;
import template.global.services.ConfigurationManager;
import template.tenants.mappers.TenantUserMapper;
import template.tenants.models.TenantUser;
import template.tenants.resolvers.TenantIdentifierResolver;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service("TenantJwtSpecialist")
public class TenantJwtSpecialist implements JwtSpecialist<TenantUser> {
    static Integer zoneOffSet = -5;
    private final ConfigurationManager authenticationConfigurationManager;
    private final TenantUserMapper tenantUserMapper;
    private final TenantIdentifierResolver currentTenant;

    public TenantJwtSpecialist(
        ConfigurationManager authenticationConfigurationManager,
        TenantUserMapper tenantUserMapper,
        TenantIdentifierResolver currentTenant) {
        this.authenticationConfigurationManager =
            authenticationConfigurationManager;
        this.tenantUserMapper = tenantUserMapper;
        this.currentTenant = currentTenant;
    }

    @Override
    public String generate(TenantUser user, String scopeList) {
        ConfigurationModel jwtExpiration =
            authenticationConfigurationManager.findByKey(
                AuthenticationVariable.JWT_EXPIRATION);
        ConfigurationModel jwtSecret =
            authenticationConfigurationManager.findByKey(
                AuthenticationVariable.JWT_SECRET);

        if (jwtExpiration == null) {
            throw new KnownServerException("JWT expiration isn't sets.");
        }

        if (jwtSecret == null) {
            throw new KnownServerException("JWT secret isn't set.");
        }

        LocalDateTime expiration =
            LocalDateTime.now()
                .plusMinutes(Long.parseLong(jwtExpiration.getValue()));
        String jwt;

        try {
            jwt = Jwts.builder().setSubject(user.getEmail())
                .claim("userDetails", tenantUserMapper.toDto(user))
                .claim("scopes", scopeList.isEmpty() ? "" : scopeList)
                .claim("tenantId",
                    currentTenant.resolveCurrentTenantIdentifier())
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getValue())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(
                    expiration.toInstant(ZoneOffset.ofHours(zoneOffSet))))
                .compact();
        } catch (Exception exception) {
            throw new UnknownServerException("Server error generating jwt.");
        }

        return jwt;
    }

    @Override
    public Claims validate(String token) {
        Claims claims;

        try {
            ConfigurationModel jwtSecret =
                authenticationConfigurationManager.findByKey(
                    AuthenticationVariable.JWT_SECRET);

            claims = Jwts.parser().setSigningKey(jwtSecret.getValue())
                .parseClaimsJws(token).getBody();
        } catch (MalformedJwtException |
                 SignatureException malformedJwtException) {
            throw new InvalidJwtException();
        } catch (ExpiredJwtException expiredJwtException) {
            throw new template.authentication.exceptions.ExpiredJwtException();
        } catch (IllegalArgumentException illegalArgumentException) {
            throw new KnownServerException("JWT service not fully setup.");
        } catch (Exception exception) {
            throw new UnknownServerException(exception.getMessage());
        }

        return claims;
    }
}