package template.authentication.controllers;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.authentication.dtos.JwtDto;
import template.authentication.dtos.SimpleUserLoginDto;
import template.authentication.exceptions.InvalidJwtException;
import template.authentication.helpers.JwtSpecialist;
import template.authentication.mappers.JwtMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.models.Jwt;
import template.authentication.services.UserLoginHandler;
import template.internal.models.InternalUser;

@RestController
@RequestMapping("internal/authentication/jwt")
public class InternalJwtController {
    private final UserLoginHandler<InternalUserPassword> simpleUserLogin;
    private final JwtMapper jwtMapper;
    private final JwtSpecialist<InternalUser> simpleJwtSpecialist;

    public InternalJwtController(
        @Qualifier("InternalUserLogin")
        UserLoginHandler<InternalUserPassword> simpleUserLogin,
        JwtMapper jwtMapper,
        @Qualifier("InternalJwtSpecialist")
        JwtSpecialist<InternalUser> simpleJwtSpecialist) {
        this.simpleUserLogin = simpleUserLogin;
        this.jwtMapper = jwtMapper;
        this.simpleJwtSpecialist = simpleJwtSpecialist;
    }

    @PostMapping
    public ResponseEntity<JwtDto> generate(
        @RequestBody SimpleUserLoginDto simpleUserLoginDto) {
        Jwt jwt =
            simpleUserLogin.jwtProvider(simpleUserLoginDto.email,
                simpleUserLoginDto.password);

        JwtDto jwtDto = jwtMapper.toDto(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(jwtDto);
    }

    @GetMapping
    public Claims validate(
        @RequestHeader(HttpHeaders.AUTHORIZATION) String bearerToken) {
        String[] jwt = bearerToken.split("Bearer ");
        if (jwt.length != 2) {
            throw new InvalidJwtException();
        }

        return simpleJwtSpecialist.validate(jwt[1]);
    }
}