package template.authorization.repositories;

import template.authorization.entities.TenantPermissionEntity;
import template.database.repositories.ListRepository;
import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;

import java.util.List;
import java.util.Optional;

public interface TenantPermissionRepository
    extends QueryRepository<TenantPermissionEntity, Integer>,
    Repository<TenantPermissionEntity>,
    ListRepository<TenantPermissionEntity> {
    Optional<TenantPermissionEntity> getById(Integer id);

    List<TenantPermissionEntity> getByIdList(List<Integer> ids);
}
