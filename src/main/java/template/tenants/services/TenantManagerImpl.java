package template.tenants.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.tenants.entities.TenantEntity;
import template.tenants.events.publishers.TenantEventPublisher;
import template.tenants.exceptions.InvalidTenantEmailException;
import template.tenants.exceptions.TenantNotFoundException;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.repositories.TenantRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TenantManagerImpl implements TenantManager {
    private final TenantRepository tenantRepository;
    private final TenantMapper tenantMapper;
    private final TenantEventPublisher tenantEventPublisher;

    public TenantManagerImpl(TenantRepository tenantRepository,
                             TenantMapper tenantMapper,
                             TenantEventPublisher tenantEventPublisher) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
        this.tenantEventPublisher = tenantEventPublisher;
    }

    @Override
    public QueryResult<Tenant> getList(Query<UUID> query) {
        Query<UUID> validquery = new Query<>(query);

        List<TenantEntity> tenantList =
            tenantRepository.getList(validquery);

        QueryResult<Tenant> result = new QueryResult<>();
        result.setData(tenantMapper.toTenantModelList(tenantList));
        result.getMeta().setPageCount(tenantList.size());

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        result.getMeta().setCount(tenantRepository.count(validquery));

        return result;
    }

    @Override
    public Tenant getById(UUID id) {
        Optional<TenantEntity> tenant = tenantRepository.getById(id);

        if (tenant.isEmpty()) {
            throw new TenantNotFoundException();
        }

        return tenantMapper.toTenantModel(tenant.get());
    }

    @Override
    public Tenant getByEmail(String email) {
        Optional<TenantEntity> tenant = tenantRepository.getByEmail(email);

        if (tenant.isEmpty()) {
            throw new TenantNotFoundException();
        }

        return tenantMapper.toTenantModel(tenant.get());
    }

    @Override
    public Tenant getBySubdomain(String subdomain) {
        Optional<TenantEntity> tenant =
            tenantRepository.getBySubdomain(subdomain);

        if (tenant.isEmpty()) {
            throw new TenantNotFoundException();
        }

        return tenantMapper.toTenantModel(tenant.get());
    }

    @Override
    public QueryResult<Tenant> getSingle(Query<UUID> query) {
        Query<UUID> validquery = new Query<>(query);

        Optional<TenantEntity> tenant =
            tenantRepository.getSingle(validquery);

        if (tenant.isEmpty()) {
            throw new TenantNotFoundException();
        }

        QueryResult<Tenant> result = new QueryResult<>();
        List<Tenant> data = new ArrayList<>();
        data.add(tenantMapper.toTenantModel(tenant.get()));
        result.setData(data);
        result.getMeta().setCount(1);
        result.getMeta().setPageCount(1);

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        return result;
    }

    @Override
    public Tenant create(Tenant tenantModel) {
        if (!tenantModel.checkIfValidEmail()) {
            throw new InvalidTenantEmailException();
        }

        tenantModel.setSubdomainFromEmail();

        TenantEntity newTenant = tenantMapper.tenantModelToTenant(tenantModel);

        tenantRepository.save(newTenant);

        tenantModel.setId(newTenant.getId());

        tenantEventPublisher.publishTenantCreatedEvent(tenantModel);

        return tenantMapper.toTenantModel(newTenant);
    }

    @Override
    public Tenant update(UUID id, Tenant tenantModel) {
        TenantEntity entity = tenantMapper.tenantModelToTenant(tenantModel);
        entity.setId(id);

        tenantRepository.save(entity);

        tenantEventPublisher.publishTenantUpdatedEvent(tenantModel);

        return tenantModel;
    }

    @Override
    public Tenant updatePartial(UUID id, Tenant tenantModel) {
        Optional<TenantEntity> getEntity = tenantRepository.getById(id);

        if (getEntity.isEmpty()) {
            throw new TenantNotFoundException();
        }

        TenantEntity foundEntity = getEntity.get();
        tenantModel.setId(foundEntity.getId());

        tenantMapper.update(foundEntity, tenantModel);
        tenantRepository.save(foundEntity);

        tenantModel = tenantMapper.toTenantModel(foundEntity);
        tenantEventPublisher.publishTenantUpdatedEvent(tenantModel);

        return tenantModel;
    }

    @Override
    public void delete(UUID id) {
        Optional<TenantEntity> getEntity = tenantRepository.getById(id);

        if (getEntity.isEmpty()) {
            throw new TenantNotFoundException();
        }

        tenantRepository.delete(getEntity.get());

        Tenant tenantModel = tenantMapper.toTenantModel(getEntity.get());

        tenantEventPublisher.publishTenantDeletedEvent(tenantModel);
    }
}