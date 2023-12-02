package template.authentication.helpers;

import io.jsonwebtoken.Claims;

public interface JwtSpecialist<T> {
    String generate(T user, String scopeList);

    Claims validate(String token);
}