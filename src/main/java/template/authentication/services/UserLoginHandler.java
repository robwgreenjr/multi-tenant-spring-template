package template.authentication.services;

import template.authentication.models.InternalUserPassword;
import template.authentication.models.Jwt;

public interface UserLoginHandler {
    Jwt jwtProvider(String identifier, String password);

    InternalUserPassword login(String identifier, String password);
}