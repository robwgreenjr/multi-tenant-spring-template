package template.authorization.services;

import template.authorization.models.TenantPermission;
import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;

import java.util.List;

public interface TenantPermissionManager
    extends QueryManager<TenantPermission, Integer>,
    Manager<TenantPermission, Integer>,
    ListManager<TenantPermission> {
    TenantPermission getById(Integer id);

    List<TenantPermission> getByIdList(List<Integer> idList);
}