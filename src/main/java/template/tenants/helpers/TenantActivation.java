package template.tenants.helpers;

import template.tenants.models.TenantUser;

public interface TenantActivation {
    void setInitialAuthorization(TenantUser tenantUser);
}
