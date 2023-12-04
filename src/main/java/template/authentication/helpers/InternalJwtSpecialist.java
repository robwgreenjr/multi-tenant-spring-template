package template.authentication.helpers;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import template.authentication.constants.AuthenticationVariable;
import template.authentication.exceptions.InvalidJwtException;
import template.database.models.DatabaseConnectionContext;
import template.global.exceptions.KnownServerException;
import template.global.exceptions.UnknownServerException;
import template.global.models.ConfigurationModel;
import template.global.services.ConfigurationManager;
import template.internal.mappers.InternalUserMapper;
import template.internal.models.InternalUser;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.UUID;

@Service("InternalJwtSpecialist")
public class InternalJwtSpecialist implements JwtSpecialist<InternalUser> {
    static Integer zoneOffSet = -5;
    private final ConfigurationManager authenticationConfigurationManager;
    private final InternalUserMapper userMapper;

    public InternalJwtSpecialist(
        ConfigurationManager authenticationConfigurationManager,
        InternalUserMapper userMapper) {
        this.authenticationConfigurationManager =
            authenticationConfigurationManager;
        this.userMapper = userMapper;
    }


    @Override
    public String generate(InternalUser user, String scopeList) {
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
            String[] findTenantId =
                DatabaseConnectionContext.getCurrentDatabaseConnection()
                    .getDataSource()
                    .getJdbcUrl().split("/");
            UUID tenantId = null;
            try {
                tenantId =
                    UUID.fromString(findTenantId[findTenantId.length - 1]);
            } catch (Exception ignore) {
                // If there is an error then we aren't dealing with a proper tenant ID
            }

            jwt = Jwts.builder().setSubject(user.getEmail())
                .claim("userDetails",
                    userMapper.toDto(user))
                .claim("scopes", scopeList.isEmpty() ? "" : scopeList)
                .claim("tenantId", tenantId == null ? "" : tenantId.toString())
                .signWith(SignatureAlgorithm.HS256, jwtSecret.getValue())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(Date.from(expiration.toInstant(
                    ZoneOffset.ofHours(zoneOffSet))))
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