package template.internal.repositories;

import template.database.repositories.ListRepository;
import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;
import template.internal.entities.InternalUserEntity;

import java.util.Optional;

public interface InternalUserRepository extends
    QueryRepository<InternalUserEntity, Integer>,
    Repository<InternalUserEntity>,
    ListRepository<InternalUserEntity, Integer> {
    Optional<InternalUserEntity> getById(Integer id);
}
