package template.authentication.services;

import template.authentication.models.Jwt;

public interface UserLoginHandler<T> {
    Jwt jwtProvider(String identifier, String password);

    T login(String identifier, String password);
}