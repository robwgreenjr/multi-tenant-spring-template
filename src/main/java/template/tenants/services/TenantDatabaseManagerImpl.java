package template.tenants.services;

import com.zaxxer.hikari.HikariDataSource;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.database.services.DatabaseConnection;
import template.global.services.StringEncoder;
import template.tenants.entities.TenantDatabaseEntity;
import template.tenants.events.publishers.TenantDatabaseEventPublisher;
import template.tenants.mappers.TenantDatabaseMapper;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.models.TenantDatabase;
import template.tenants.repositories.TenantDatabaseRepository;

import java.util.UUID;

@Service
@Transactional
public class TenantDatabaseManagerImpl implements TenantDatabaseManager {
    private final TenantDatabaseRepository tenantDatabaseRepository;
    private final TenantDatabaseMapper tenantDatabaseMapper;
    private final TenantMapper tenantMapper;
    private final DatabaseConnection databaseConnection;
    private final StringEncoder cryptoEncoder;
    private final TenantDatabaseEventPublisher tenantDatabaseEventPublisher;

    public TenantDatabaseManagerImpl(
        TenantDatabaseRepository tenantDatabaseRepository,
        TenantDatabaseMapper tenantDatabaseMapper,
        TenantMapper tenantMapper,
        DatabaseConnection databaseConnection,
        @Qualifier("CryptoEncoder")
        StringEncoder cryptoEncoder,
        TenantDatabaseEventPublisher tenantDatabaseEventPublisher) {
        this.tenantDatabaseRepository = tenantDatabaseRepository;
        this.tenantDatabaseMapper = tenantDatabaseMapper;
        this.tenantMapper = tenantMapper;
        this.databaseConnection = databaseConnection;
        this.cryptoEncoder = cryptoEncoder;
        this.tenantDatabaseEventPublisher = tenantDatabaseEventPublisher;
    }

    @Override
    public TenantDatabase create(Tenant tenantModel) {
        TenantDatabaseEntity tenantDatabase = new TenantDatabaseEntity();
        tenantDatabase.setTenant(tenantMapper.tenantModelToTenant(tenantModel));
        tenantDatabase.setUsername(
            cryptoEncoder.encode(UUID.randomUUID().toString()));
        tenantDatabase.setPassword(
            cryptoEncoder.encode(UUID.randomUUID().toString()));

        HikariDataSource dataSource = databaseConnection.getDefault();

        String url = dataSource.getJdbcUrl()
            .replace("main", tenantModel.getId().toString());
        if (dataSource.getJdbcUrl().contains("test")) {
            url = dataSource.getJdbcUrl()
                .replace("test", tenantModel.getId().toString());
        }
        tenantDatabase.setUrl(url);

        tenantDatabaseRepository.save(tenantDatabase);

        tenantDatabaseEventPublisher.publishTenantCreatedEvent(
            tenantDatabaseMapper.toTenantDatabase(tenantDatabase));

        return tenantDatabaseMapper.toTenantDatabase(
            tenantDatabase);
    }

    @Override
    public TenantDatabase getByTenant(Tenant tenantModel) {
        TenantDatabaseEntity tenantDatabase =
            tenantDatabaseRepository.getByTenant(
                tenantMapper.tenantModelToTenant(tenantModel));

        return tenantDatabaseMapper.toTenantDatabase(tenantDatabase);
    }
}
