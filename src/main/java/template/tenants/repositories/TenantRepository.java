package template.tenants.repositories;

import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;
import template.tenants.entities.TenantEntity;

import java.util.Optional;
import java.util.UUID;

public interface TenantRepository extends QueryRepository<TenantEntity, UUID>,
    Repository<TenantEntity> {
    Optional<TenantEntity> getByEmail(String email);

    Optional<TenantEntity> getById(UUID id);

    Optional<TenantEntity> getBySubdomain(String subdomain);
}
