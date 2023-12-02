package template.authentication.helpers;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;
import template.authentication.constants.AuthenticationVariable;
import template.authentication.exceptions.InvalidJwtException;
import template.global.exceptions.KnownServerException;
import template.global.exceptions.UnknownServerException;
import template.global.models.ConfigurationModel;
import template.global.services.ConfigurationManager;
import template.tenants.models.TenantUser;

@Service("TenantJwtSpecialist")
public class TenantJwtSpecialist implements JwtSpecialist<TenantUser> {
    static Integer zoneOffSet = -5;
    private final ConfigurationManager authenticationConfigurationManager;
//    private final UserMapper userMapper;

    public TenantJwtSpecialist(
        ConfigurationManager authenticationConfigurationManager) {
        this.authenticationConfigurationManager =
            authenticationConfigurationManager;
//        this.userMapper = userMapper;
    }

    @Override
    public String generate(TenantUser user, String scopeList) {
        return null;
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