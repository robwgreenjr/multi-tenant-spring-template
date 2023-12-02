package template.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import template.authentication.entities.TenantUserPasswordEntity;

import java.util.Optional;

public interface ITenantUserPasswordRepository
    extends JpaRepository<TenantUserPasswordEntity, Integer> {
    Optional<TenantUserPasswordEntity> getByUserEmail(String email);
}