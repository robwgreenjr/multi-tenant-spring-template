package template.authorization.services;

import template.authorization.models.InternalPermission;
import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;

import java.util.List;

public interface InternalPermissionManager
    extends QueryManager<InternalPermission, Integer>,
    Manager<InternalPermission, Integer>,
    ListManager<InternalPermission, Integer> {
    InternalPermission getById(Integer id);

    List<InternalPermission> getByIdList(List<Integer> idList);
}