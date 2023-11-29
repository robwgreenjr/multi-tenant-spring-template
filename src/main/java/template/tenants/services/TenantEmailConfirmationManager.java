package template.tenants.services;

import template.tenants.models.TenantEmailConfirmation;

public interface TenantEmailConfirmationManager {
    TenantEmailConfirmation create(TenantEmailConfirmation tenantEmailConfirmationModel);

    TenantEmailConfirmation getByToken(String token);
}
