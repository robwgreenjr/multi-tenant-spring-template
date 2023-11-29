package template.tenants.services;

import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;
import template.tenants.models.TenantUser;

public interface TenantUserManager extends
    QueryManager<TenantUser, Integer>,
    ListManager<TenantUser, Integer>,
    Manager<TenantUser, Integer> {
}
