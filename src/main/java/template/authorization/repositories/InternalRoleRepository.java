package template.authorization.repositories;

import template.authorization.entities.InternalRoleEntity;
import template.database.repositories.ListRepository;
import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;

import java.util.List;
import java.util.Optional;

public interface InternalRoleRepository
    extends QueryRepository<InternalRoleEntity, Integer>,
    ListRepository<InternalRoleEntity>,
    Repository<InternalRoleEntity> {
    List<InternalRoleEntity> getByIdList(List<Integer> ids);

    Optional<InternalRoleEntity> getById(Integer id);

    Optional<InternalRoleEntity> getByName(String name);

    List<InternalRoleEntity> getListByUsersId(Integer id);
}
