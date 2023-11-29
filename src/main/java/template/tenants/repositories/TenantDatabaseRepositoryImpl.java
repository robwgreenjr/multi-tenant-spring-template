package template.tenants.repositories;

import org.springframework.stereotype.Repository;
import template.database.models.Query;
import template.database.services.QueryBuilder;
import template.tenants.entities.TenantDatabaseEntity;
import template.tenants.entities.TenantEntity;

import java.util.List;
import java.util.Optional;

@Repository
public class TenantDatabaseRepositoryImpl implements TenantDatabaseRepository {
    private final ITenantDatabaseRepository tenantDatabaseRepository;
    private final QueryBuilder<TenantDatabaseEntity, Integer> queryBuilder;

    public TenantDatabaseRepositoryImpl(
        ITenantDatabaseRepository tenantDatabaseRepository,
        QueryBuilder<TenantDatabaseEntity, Integer> queryBuilder) {
        this.tenantDatabaseRepository = tenantDatabaseRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(TenantDatabaseEntity.class, query);
    }

    @Override
    public void delete(TenantDatabaseEntity tenant) {
        tenantDatabaseRepository.delete(tenant);
    }

    @Override
    public Optional<TenantDatabaseEntity> getById(Integer id) {
        return tenantDatabaseRepository.findById(id);
    }

    @Override
    public TenantDatabaseEntity getByTenant(TenantEntity tenant) {
        return tenantDatabaseRepository.findByTenant(tenant);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<TenantDatabaseEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(TenantDatabaseEntity.class, query);
    }

    @Override
    public TenantDatabaseEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public TenantDatabaseEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<TenantDatabaseEntity> getSingle(Query<Integer> query) {
        TenantDatabaseEntity entity =
            queryBuilder.getSingle(TenantDatabaseEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(TenantDatabaseEntity tenant) {
        tenantDatabaseRepository.save(tenant);
    }
}
