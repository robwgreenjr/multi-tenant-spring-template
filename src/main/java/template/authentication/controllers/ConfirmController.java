package template.authentication.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template.authentication.dtos.ConfirmTenantDto;
import template.authentication.dtos.ConfirmTenantResponseDto;
import template.tenants.models.Tenant;
import template.tenants.services.TenantManager;

@RestController
@RequestMapping("authentication/confirm")
public class ConfirmController {
    private final TenantManager tenantManager;

    public ConfirmController(TenantManager tenantManager) {
        this.tenantManager = tenantManager;
    }

    @PostMapping
    public ConfirmTenantResponseDto validate(
        @RequestBody ConfirmTenantDto confirmTenantDto) {
        Tenant tenantModel =
            tenantManager.getBySubdomain(confirmTenantDto.subdomain);

        ConfirmTenantResponseDto confirmTenantResponseDto =
            new ConfirmTenantResponseDto();
        confirmTenantResponseDto.tenantId = tenantModel.getId().toString();

        return confirmTenantResponseDto;
    }
}
