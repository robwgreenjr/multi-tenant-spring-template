package template.tenants.services;

import template.tenants.models.Tenant;

import java.sql.SQLException;

public interface TenantRegistration {
    void register(Tenant tenantModel);

    void buildNewTenant(String confirmationToken) throws SQLException;
}
