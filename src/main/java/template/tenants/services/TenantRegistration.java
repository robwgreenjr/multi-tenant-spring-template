package template.tenants.services;

import template.tenants.models.Tenant;
import template.tenants.models.TenantActivationConfirmation;

public interface TenantRegistration {
    void register(Tenant tenant);

    void activateTenant(
        TenantActivationConfirmation tenantActivationConfirmation);
}
