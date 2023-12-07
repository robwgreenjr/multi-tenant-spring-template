package template.tenants.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.tenants.entities.TenantUserEntity;

import java.util.Optional;

public interface ITenantUserRepository extends
    JpaRepository<TenantUserEntity, Integer>,
    JpaSpecificationExecutor<TenantUserEntity> {
    Optional<TenantUserEntity> getByEmail(String email);
}
