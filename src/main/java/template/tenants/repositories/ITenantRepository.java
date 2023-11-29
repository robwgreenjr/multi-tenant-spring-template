package template.tenants.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.tenants.entities.TenantEntity;

import java.util.Optional;
import java.util.UUID;

public interface ITenantRepository
    extends JpaRepository<TenantEntity, UUID>, JpaSpecificationExecutor<TenantEntity> {
    Optional<TenantEntity> getByEmail(String email);

    Optional<TenantEntity> getBySubdomain(String subdomain);
}