package template.tenants.services;

import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;
import template.tenants.models.TenantUser;

import java.util.Optional;

public interface TenantUserManager extends
    QueryManager<TenantUser, Integer>,
    ListManager<TenantUser>,
    Manager<TenantUser, Integer> {
    Optional<TenantUser> findByEmail(String email);
}
