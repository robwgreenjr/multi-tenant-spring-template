package template.tenants.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.tenants.entities.TenantEmailConfirmationEntity;
import template.tenants.entities.TenantEntity;

import java.util.UUID;

public interface ITenantEmailConfirmationRepository
    extends JpaRepository<TenantEmailConfirmationEntity, UUID>,
    JpaSpecificationExecutor<TenantEmailConfirmationEntity> {
    TenantEmailConfirmationEntity findTenantEmailConfirmationByToken(UUID token);

    void deleteByTenant(TenantEntity tenant);
}