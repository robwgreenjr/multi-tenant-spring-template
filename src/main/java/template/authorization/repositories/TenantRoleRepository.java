package template.authorization.repositories;

import template.authorization.entities.TenantRoleEntity;
import template.database.repositories.ListRepository;
import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;

import java.util.List;
import java.util.Optional;

public interface TenantRoleRepository
    extends QueryRepository<TenantRoleEntity, Integer>,
    ListRepository<TenantRoleEntity, Integer>,
    Repository<TenantRoleEntity> {
    List<TenantRoleEntity> getByIdList(List<Integer> ids);

    Optional<TenantRoleEntity> getById(Integer id);

    Optional<TenantRoleEntity> getByName(String name);

    List<TenantRoleEntity> getListByUsersId(Integer id);
}
