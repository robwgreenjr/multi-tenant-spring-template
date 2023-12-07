package template.tenants.repositories;

import org.springframework.stereotype.Repository;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.database.services.QueryBuilder;
import template.tenants.entities.TenantEmailConfirmationEntity;
import template.tenants.entities.TenantEntity;

import java.util.Optional;
import java.util.UUID;

@Repository
public class TenantEmailConfirmationRepositoryImpl
    implements TenantEmailConfirmationRepository {
    private final ITenantEmailConfirmationRepository
        tenantEmailConfirmationRepository;
    private final QueryBuilder<TenantEmailConfirmationEntity, UUID>
        queryBuilder;

    public TenantEmailConfirmationRepositoryImpl(
        ITenantEmailConfirmationRepository tenantEmailConfirmationRepository,
        QueryBuilder<TenantEmailConfirmationEntity, UUID> queryBuilder) {
        this.tenantEmailConfirmationRepository =
            tenantEmailConfirmationRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<UUID> query) {
        return queryBuilder.getCount(TenantEmailConfirmationEntity.class,
            query);
    }

    @Override
    public void delete(TenantEmailConfirmationEntity tenantEmailConfirmation) {
        tenantEmailConfirmationRepository.delete(tenantEmailConfirmation);
    }

    @Override
    public void deleteByTenant(TenantEntity tenant) {
        tenantEmailConfirmationRepository.deleteByTenant(tenant);
    }

    @Override
    public Optional<TenantEmailConfirmationEntity> getById(UUID id) {
        return tenantEmailConfirmationRepository.findById(id);
    }

    @Override
    public Optional<TenantEmailConfirmationEntity> getByToken(UUID token) {
        TenantEmailConfirmationEntity tenantEmailConfirmation =
            tenantEmailConfirmationRepository.findTenantEmailConfirmationByToken(
                token);

        if (tenantEmailConfirmation == null) {
            return Optional.empty();
        }

        return Optional.of(tenantEmailConfirmation);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<UUID> query) {
        return null;
    }

    @Override
    public QueryResult<TenantEmailConfirmationEntity> getList(
        Query<UUID> query) {
        return queryBuilder.getList(TenantEmailConfirmationEntity.class,
            query);
    }

    @Override
    public TenantEmailConfirmationEntity getNext(Query<UUID> query) {
        return null;
    }

    @Override
    public TenantEmailConfirmationEntity getPrevious(Query<UUID> query) {
        return null;
    }

    @Override
    public Optional<TenantEmailConfirmationEntity> getSingle(
        Query<UUID> query) {
        TenantEmailConfirmationEntity entity =
            queryBuilder.getSingle(TenantEmailConfirmationEntity.class,
                query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(TenantEmailConfirmationEntity tenantEmailConfirmation) {
        tenantEmailConfirmationRepository.save(tenantEmailConfirmation);
    }
}
