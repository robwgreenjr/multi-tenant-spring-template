package template.tenants.repositories;

import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;
import template.tenants.entities.TenantEmailConfirmationEntity;
import template.tenants.entities.TenantEntity;

import java.util.Optional;
import java.util.UUID;

public interface TenantEmailConfirmationRepository
    extends QueryRepository<TenantEmailConfirmationEntity, UUID>,
    Repository<TenantEmailConfirmationEntity> {
    Optional<TenantEmailConfirmationEntity> getById(UUID id);

    Optional<TenantEmailConfirmationEntity> getByToken(UUID token);

    void deleteByTenant(TenantEntity tenant);
}
