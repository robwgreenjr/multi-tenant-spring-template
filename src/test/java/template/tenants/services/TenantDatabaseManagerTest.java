package template.tenants.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.global.services.StringEncoder;
import template.tenants.entities.TenantEntity;
import template.tenants.events.publishers.TenantDatabaseEventPublisher;
import template.tenants.mappers.TenantDatabaseMapper;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.repositories.TenantDatabaseRepository;

import static org.mockito.Mockito.*;

public class TenantDatabaseManagerTest {
    private final TenantDatabaseRepository tenantDatabaseRepository =
        Mockito.mock(TenantDatabaseRepository.class);
    private final TenantDatabaseMapper tenantDatabaseMapper =
        Mockito.mock(TenantDatabaseMapper.class);
    private final TenantMapper tenantMapper =
        Mockito.mock(TenantMapper.class);
    private final TenantDatabaseEventPublisher tenantDatabaseEventPublisher =
        Mockito.mock(TenantDatabaseEventPublisher.class);
    private final StringEncoder cryptoEncoder =
        Mockito.mock(StringEncoder.class);

    private TenantDatabaseManager tenantDatabaseManager;

    @BeforeEach
    void init() {
        tenantDatabaseManager =
            new TenantDatabaseManagerImpl(tenantDatabaseRepository,
                tenantDatabaseMapper, tenantMapper, cryptoEncoder,
                tenantDatabaseEventPublisher);
    }

    @Test
    public void givenTenantDatabase_whenCreated_shouldTriggerCreateEvent() {
        when(tenantMapper.toEntity(Mockito.any())).thenReturn(
            new TenantEntity());

        tenantDatabaseManager.create(new Tenant());
        verify(tenantDatabaseEventPublisher,
            times(1)).publishTenantCreatedEvent(Mockito.any());
    }
}
