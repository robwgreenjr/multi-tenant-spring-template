package template.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import template.authentication.entities.InternalUserPasswordEntity;

import java.util.Optional;

public interface ITenantUserPasswordRepository
    extends JpaRepository<InternalUserPasswordEntity, Integer> {
    Optional<InternalUserPasswordEntity> getByUserEmail(String email);
}