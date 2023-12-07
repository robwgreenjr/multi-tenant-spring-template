package template.authorization.services;

import template.authorization.models.TenantRole;
import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;

import java.util.List;

public interface TenantRoleManager
    extends QueryManager<TenantRole, Integer>,
    ListManager<TenantRole>,
    Manager<TenantRole, Integer> {

    TenantRole getById(Integer id);

    List<TenantRole> getByIdList(List<Integer> ids);

    List<TenantRole> getListByUserId(Integer id);

    TenantRole getByName(String name);
}