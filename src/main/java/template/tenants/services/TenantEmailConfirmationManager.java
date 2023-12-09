package template.tenants.services;

import template.tenants.models.TenantEmailConfirmation;

import java.util.Optional;
import java.util.UUID;

public interface TenantEmailConfirmationManager {
    TenantEmailConfirmation create(
        TenantEmailConfirmation tenantEmailConfirmation);

    Optional<TenantEmailConfirmation> getByToken(UUID token);

    void delete(TenantEmailConfirmation tenantEmailConfirmation);
}
