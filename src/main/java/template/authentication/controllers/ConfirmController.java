package template.authentication.controllers;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import template.authentication.dtos.ConfirmTenantDto;
import template.authentication.dtos.ConfirmTenantResponseDto;
import template.tenants.exceptions.TenantNotFoundException;
import template.tenants.models.Tenant;
import template.tenants.services.TenantManager;

import java.util.Optional;

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
        Optional<Tenant> tenant =
            tenantManager.getBySubdomain(confirmTenantDto.subdomain);

        if (tenant.isEmpty()) {
            throw new TenantNotFoundException();
        }

        ConfirmTenantResponseDto confirmTenantResponseDto =
            new ConfirmTenantResponseDto();
        confirmTenantResponseDto.tenantId = tenant.get().getId().toString();

        return confirmTenantResponseDto;
    }
}
