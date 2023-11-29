package template.tenants.repositories;

import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;
import template.tenants.entities.TenantDatabaseEntity;
import template.tenants.entities.TenantEntity;

import java.util.Optional;

public interface TenantDatabaseRepository
    extends QueryRepository<TenantDatabaseEntity, Integer>,
    Repository<TenantDatabaseEntity> {
    Optional<TenantDatabaseEntity> getById(Integer id);
    
    TenantDatabaseEntity getByTenant(TenantEntity tenant);
}
