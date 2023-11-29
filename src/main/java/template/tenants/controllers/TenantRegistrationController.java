package template.tenants.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import template.tenants.dtos.TenantDto;
import template.tenants.dtos.TenantEmailConfirmationDto;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.services.TenantRegistration;

import java.sql.SQLException;

@RestController
public class TenantRegistrationController {

    private final TenantMapper tenantMapper;
    private final TenantRegistration tenantRegistration;

    public TenantRegistrationController(TenantMapper tenantMapper,
                                        TenantRegistration tenantRegistration) {
        this.tenantMapper = tenantMapper;
        this.tenantRegistration = tenantRegistration;
    }

    @PostMapping("tenant/registration")
    public ResponseEntity<Void> registration(@RequestBody TenantDto tenantDto) {
        Tenant tenantModel =
            tenantMapper.tenantDtoToTenantModel(tenantDto);

        tenantRegistration.register(tenantModel);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("tenant/registration/confirmation")
    public void registrationConfirmation(
        @RequestBody
        TenantEmailConfirmationDto tenantEmailConfirmationDto)
        throws SQLException {
        tenantRegistration.buildNewTenant(tenantEmailConfirmationDto.token);
    }
}
