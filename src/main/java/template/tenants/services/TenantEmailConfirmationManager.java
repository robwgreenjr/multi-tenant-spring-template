package template.tenants.services;

import template.tenants.models.TenantEmailConfirmation;

import java.util.Optional;

public interface TenantEmailConfirmationManager {
    TenantEmailConfirmation create(
        TenantEmailConfirmation tenantEmailConfirmationModel);

    Optional<TenantEmailConfirmation> getByToken(String token);
}
