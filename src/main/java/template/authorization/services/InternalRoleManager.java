package template.authorization.services;

import template.authorization.models.InternalRole;
import template.global.services.ListManager;
import template.global.services.Manager;
import template.global.services.QueryManager;

import java.util.List;

public interface InternalRoleManager
    extends QueryManager<InternalRole, Integer>,
    ListManager<InternalRole, Integer>,
    Manager<InternalRole, Integer> {

    InternalRole getById(Integer id);

    List<InternalRole> getByIdList(List<Integer> ids);

    List<InternalRole> getListByUserId(Integer id);

    InternalRole getByName(String name);
}