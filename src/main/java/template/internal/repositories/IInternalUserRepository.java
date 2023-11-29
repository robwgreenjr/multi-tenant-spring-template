package template.internal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.internal.entities.InternalUserEntity;

public interface IInternalUserRepository extends
    JpaRepository<InternalUserEntity, Integer>,
    JpaSpecificationExecutor<InternalUserEntity> {
}
