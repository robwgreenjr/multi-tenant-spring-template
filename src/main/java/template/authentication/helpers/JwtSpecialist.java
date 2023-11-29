package template.authentication.helpers;

import io.jsonwebtoken.Claims;
import template.internal.models.InternalUser;
import template.tenants.models.TenantUser;

public interface JwtSpecialist {
    String generate(InternalUser user, String scopeList);

    String generate(TenantUser user, String scopeList);

    Claims validate(String token);
}