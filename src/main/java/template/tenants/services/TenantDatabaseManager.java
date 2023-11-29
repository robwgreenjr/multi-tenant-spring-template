package template.tenants.services;

import template.tenants.models.Tenant;
import template.tenants.models.TenantDatabase;

public interface TenantDatabaseManager {
    TenantDatabase create(Tenant tenant);

    TenantDatabase getByTenant(Tenant tenant);
}
