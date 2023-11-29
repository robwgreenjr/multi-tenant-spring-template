package template.authorization.repositories;

import template.authorization.entities.InternalPermissionEntity;
import template.database.repositories.ListRepository;
import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;

import java.util.List;
import java.util.Optional;

public interface InternalPermissionRepository
    extends QueryRepository<InternalPermissionEntity, Integer>,
    Repository<InternalPermissionEntity>,
    ListRepository<InternalPermissionEntity, Integer> {
    Optional<InternalPermissionEntity> getById(Integer id);

    List<InternalPermissionEntity> getByIdList(List<Integer> ids);
}
