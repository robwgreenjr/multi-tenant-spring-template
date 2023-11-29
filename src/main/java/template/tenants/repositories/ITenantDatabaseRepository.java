package template.tenants.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.tenants.entities.TenantDatabaseEntity;
import template.tenants.entities.TenantEntity;

public interface ITenantDatabaseRepository
    extends JpaRepository<TenantDatabaseEntity, Integer>,
    JpaSpecificationExecutor<TenantDatabaseEntity> {
    TenantDatabaseEntity findByTenant(TenantEntity tenant);
}