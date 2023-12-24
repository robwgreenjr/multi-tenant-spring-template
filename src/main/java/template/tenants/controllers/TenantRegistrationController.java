package template.tenants.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import template.database.exceptions.NotFoundDataException;
import template.tenants.dtos.TenantActivationConfirmationDto;
import template.tenants.dtos.TenantDto;
import template.tenants.dtos.TenantEmailConfirmationDto;
import template.tenants.mappers.TenantActivationConfirmationMapper;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.models.TenantActivationConfirmation;
import template.tenants.models.TenantEmailConfirmation;
import template.tenants.services.TenantEmailConfirmationManager;
import template.tenants.services.TenantRegistration;

import java.util.Optional;
import java.util.UUID;

@RestController
public class TenantRegistrationController {
    private final TenantMapper tenantMapper;
    private final TenantRegistration tenantRegistration;
    private final TenantActivationConfirmationMapper
        tenantActivationConfirmationMapper;
    private final TenantEmailConfirmationManager tenantEmailConfirmationManager;

    public TenantRegistrationController(TenantMapper tenantMapper,
                                        TenantRegistration tenantRegistration,
                                        TenantActivationConfirmationMapper tenantActivationConfirmationMapper,
                                        TenantEmailConfirmationManager tenantEmailConfirmationManager) {
        this.tenantMapper = tenantMapper;
        this.tenantRegistration = tenantRegistration;
        this.tenantActivationConfirmationMapper =
            tenantActivationConfirmationMapper;
        this.tenantEmailConfirmationManager = tenantEmailConfirmationManager;
    }

    @PostMapping("tenant/registration")
    public ResponseEntity<Void> registration(@RequestBody TenantDto tenantDto) {
        Tenant tenant = tenantMapper.dtoToObject(tenantDto);

        tenantRegistration.register(tenant);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("tenant/registration/confirmation/{token}")
    public ResponseEntity<TenantEmailConfirmationDto> registrationConfirmation(
        @PathVariable UUID token) {
        Optional<TenantEmailConfirmation> tenantEmailConfirmation =
            tenantEmailConfirmationManager.getByToken(token);
        if (tenantEmailConfirmation.isEmpty()) {
            throw new NotFoundDataException(
                "Your confirmation token doesn't exist.");
        }

        TenantEmailConfirmationDto response = new TenantEmailConfirmationDto();
        response.tenantId = tenantEmailConfirmation.get().getTenant().getId();

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("tenant/registration/confirmation")
    public void registrationConfirmation(
        @RequestBody
        TenantActivationConfirmationDto tenantEmailConfirmationDto) {
        TenantActivationConfirmation tenantActivationConfirmation =
            tenantActivationConfirmationMapper.dtoToObject(
                tenantEmailConfirmationDto);

        tenantRegistration.activateTenant(tenantActivationConfirmation);
    }
}
