package template.authentication.controllers;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
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
import template.authentication.models.Jwt;
import template.authentication.models.TenantUserPassword;
import template.authentication.services.UserLoginHandler;
import template.tenants.exceptions.TenantNotFoundException;
import template.tenants.exceptions.TenantUserNotFoundException;
import template.tenants.models.Tenant;
import template.tenants.models.TenantUser;
import template.tenants.resolvers.TenantIdentifierResolver;
import template.tenants.services.TenantManager;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("authentication/jwt")
public class TenantJwtController {
    private final UserLoginHandler<TenantUserPassword> simpleUserLogin;
    private final JwtMapper jwtMapper;
    private final JwtSpecialist<TenantUser> simpleJwtSpecialist;
    private final TenantManager tenantManager;
    private final TenantIdentifierResolver currentTenant;

    public TenantJwtController(
        @Qualifier("TenantUserLogin")
        UserLoginHandler<TenantUserPassword> simpleUserLogin,
        JwtMapper jwtMapper,
        @Qualifier("TenantJwtSpecialist")
        JwtSpecialist<TenantUser> simpleJwtSpecialist,
        TenantManager tenantManager, TenantIdentifierResolver currentTenant) {
        this.simpleUserLogin = simpleUserLogin;
        this.jwtMapper = jwtMapper;
        this.simpleJwtSpecialist = simpleJwtSpecialist;
        this.tenantManager = tenantManager;
        this.currentTenant = currentTenant;
    }

    @PostMapping
    public ResponseEntity<JwtDto> generate(HttpServletRequest request,
                                           @RequestBody
                                           SimpleUserLoginDto simpleUserLoginDto) {
        String tenantHeaderId = request.getHeader("tenant-id");
        // TODO: May want more descriptive response if a user doesn't provide a tenantId
        if (tenantHeaderId == null || tenantHeaderId.isEmpty()) {
            throw new TenantNotFoundException();
        }

        UUID tenantId;
        try {
            tenantId = UUID.fromString(tenantHeaderId);
        } catch (Exception exception) {
            throw new TenantNotFoundException();
        }

        Optional<Tenant> tenant = tenantManager.getById(tenantId);

        if (tenant.isEmpty()) {
            throw new TenantUserNotFoundException();
        } else {
            currentTenant.setCurrentTenant(tenantId);
        }

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