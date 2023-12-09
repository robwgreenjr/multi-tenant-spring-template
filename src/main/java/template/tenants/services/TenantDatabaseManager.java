package template.tenants.services;

import template.tenants.models.Tenant;
import template.tenants.models.TenantDatabase;

import java.util.Optional;

public interface TenantDatabaseManager {
    TenantDatabase create(Tenant tenant);

    Optional<TenantDatabase> getByTenant(Tenant tenant);
}
