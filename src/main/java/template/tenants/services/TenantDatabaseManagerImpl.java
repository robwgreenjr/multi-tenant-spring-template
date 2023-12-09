package template.tenants.services;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.global.services.StringEncoder;
import template.tenants.entities.TenantDatabaseEntity;
import template.tenants.events.publishers.TenantDatabaseEventPublisher;
import template.tenants.mappers.TenantDatabaseMapper;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.models.TenantDatabase;
import template.tenants.repositories.TenantDatabaseRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class TenantDatabaseManagerImpl implements TenantDatabaseManager {
    private final TenantDatabaseRepository tenantDatabaseRepository;
    private final TenantDatabaseMapper tenantDatabaseMapper;
    private final TenantMapper tenantMapper;
    private final StringEncoder cryptoEncoder;
    private final TenantDatabaseEventPublisher tenantDatabaseEventPublisher;

    public TenantDatabaseManagerImpl(
        TenantDatabaseRepository tenantDatabaseRepository,
        TenantDatabaseMapper tenantDatabaseMapper,
        TenantMapper tenantMapper,
        @Qualifier("CryptoEncoder")
        StringEncoder cryptoEncoder,
        TenantDatabaseEventPublisher tenantDatabaseEventPublisher) {
        this.tenantDatabaseRepository = tenantDatabaseRepository;
        this.tenantDatabaseMapper = tenantDatabaseMapper;
        this.tenantMapper = tenantMapper;
        this.cryptoEncoder = cryptoEncoder;
        this.tenantDatabaseEventPublisher = tenantDatabaseEventPublisher;
    }

    @Override
    public TenantDatabase create(Tenant tenant) {
        TenantDatabaseEntity tenantDatabase = new TenantDatabaseEntity();
        tenantDatabase.setTenant(tenantMapper.toEntity(tenant));
        tenantDatabase.setUsername(
            cryptoEncoder.encode(UUID.randomUUID().toString()));
        tenantDatabase.setPassword(
            cryptoEncoder.encode(UUID.randomUUID().toString()));

        // TODO: Will need to create whole new database URL from AWS (out of scope for template)
        // TODO: Use Pulumi for this?
        tenantDatabase.setUrl("out of scope");

        tenantDatabaseRepository.save(tenantDatabase);

        tenantDatabaseEventPublisher.publishTenantCreatedEvent(
            tenantDatabaseMapper.toTenantDatabase(tenantDatabase));

        return tenantDatabaseMapper.toTenantDatabase(
            tenantDatabase);
    }

    @Override
    public Optional<TenantDatabase> getByTenant(Tenant tenant) {
        Optional<TenantDatabaseEntity> tenantDatabase =
            tenantDatabaseRepository.getByTenant(tenantMapper.toEntity(tenant));

        return tenantDatabase.map(tenantDatabaseMapper::toTenantDatabase);
    }
}
