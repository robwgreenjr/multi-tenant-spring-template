package template.tenants.repositories;

import org.springframework.stereotype.Repository;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.database.services.QueryBuilder;
import template.tenants.entities.TenantEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TenantRepositoryImpl implements TenantRepository {
    private final ITenantRepository tenantRepository;
    private final QueryBuilder<TenantEntity, UUID> queryBuilder;

    public TenantRepositoryImpl(ITenantRepository tenantRepository,
                                QueryBuilder<TenantEntity, UUID> queryBuilder) {
        this.tenantRepository = tenantRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<UUID> query) {
        return queryBuilder.getCount(TenantEntity.class, query);
    }

    @Override
    public void delete(TenantEntity tenant) {
        tenantRepository.delete(tenant);
    }

    @Override
    public QueryResult<TenantEntity> getList(Query<UUID> query) {
        return queryBuilder.getList(TenantEntity.class, query);
    }

    @Override
    public TenantEntity getNext(Query<UUID> query) {
        return null;
    }

    @Override
    public TenantEntity getPrevious(Query<UUID> query) {
        return null;
    }

    @Override
    public Optional<TenantEntity> getByEmail(String email) {
        return tenantRepository.getByEmail(email);
    }

    @Override
    public Optional<TenantEntity> getBySubdomain(String subdomain) {
        return tenantRepository.getBySubdomain(subdomain);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<UUID> query) {
        return null;
    }

    @Override
    public Optional<TenantEntity> getById(UUID id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Optional<TenantEntity> getSingle(Query<UUID> query) {
        TenantEntity tenant =
            queryBuilder.getSingle(TenantEntity.class, query);

        return tenant != null ? Optional.of(tenant) : Optional.empty();
    }

    @Override
    public void save(TenantEntity tenant) {
        tenantRepository.save(tenant);
    }

}
