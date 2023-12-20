package template.tenants.repositories;

import template.database.repositories.ListRepository;
import template.database.repositories.QueryRepository;
import template.database.repositories.Repository;
import template.tenants.entities.TenantUserEntity;

import java.util.Optional;

public interface TenantUserRepository extends
    QueryRepository<TenantUserEntity, Integer>,
    Repository<TenantUserEntity>,
    ListRepository<TenantUserEntity> {
    Optional<TenantUserEntity> getById(Integer id);

    Optional<TenantUserEntity> getByEmail(String email);
}
