package template.internal.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.internal.entities.InternalUserEntity;

import java.util.Optional;

public interface IInternalUserRepository extends
    JpaRepository<InternalUserEntity, Integer>,
    JpaSpecificationExecutor<InternalUserEntity> {
    Optional<InternalUserEntity> findByEmail(String email);
}
