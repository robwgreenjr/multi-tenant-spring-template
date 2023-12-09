package template.tenants.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import template.tenants.dtos.TenantActivationConfirmationDto;
import template.tenants.dtos.TenantDto;
import template.tenants.mappers.TenantActivationConfirmationMapper;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.models.TenantActivationConfirmation;
import template.tenants.services.TenantRegistration;

@RestController
public class TenantRegistrationController {
    private final TenantMapper tenantMapper;
    private final TenantRegistration tenantRegistration;
    private final TenantActivationConfirmationMapper
        tenantActivationConfirmationMapper;

    public TenantRegistrationController(TenantMapper tenantMapper,
                                        TenantRegistration tenantRegistration,
                                        TenantActivationConfirmationMapper tenantActivationConfirmationMapper) {
        this.tenantMapper = tenantMapper;
        this.tenantRegistration = tenantRegistration;
        this.tenantActivationConfirmationMapper =
            tenantActivationConfirmationMapper;
    }

    @PostMapping("tenant/registration")
    public ResponseEntity<Void> registration(@RequestBody TenantDto tenantDto) {
        Tenant tenant = tenantMapper.dtoToObject(tenantDto);

        tenantRegistration.register(tenant);

        return ResponseEntity.status(HttpStatus.OK).build();
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
