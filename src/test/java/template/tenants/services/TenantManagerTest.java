package template.tenants.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.tenants.entities.TenantEntity;
import template.tenants.events.publishers.TenantEventPublisher;
import template.tenants.mappers.TenantMapper;
import template.tenants.models.Tenant;
import template.tenants.repositories.TenantRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class TenantManagerTest {
    private final TenantRepository tenantRepository =
        Mockito.mock(TenantRepository.class);
    private final TenantMapper tenantMapper =
        Mockito.mock(TenantMapper.class);
    private final TenantEventPublisher tenantEventPublisher =
        Mockito.mock(TenantEventPublisher.class);
    private final Tenant tenant = Mockito.mock(Tenant.class);

    private TenantManager tenantManager;

    @BeforeEach
    void init() {
        tenantManager = new TenantManagerImpl(tenantRepository, tenantMapper,
            tenantEventPublisher);
    }

    @Test
    public void givenTenant_whenCreated_shouldTriggerCreateEvent() {
        when(tenantMapper.toEntity(Mockito.any())).thenReturn(
            new TenantEntity());

        when(tenant.checkIfValidEmail()).thenReturn(true);

        tenantManager.create(tenant);
        verify(tenantEventPublisher,
            times(1)).publishTenantCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenant_whenUpdate_shouldTriggerUpdateEvent() {
        when(tenantMapper.toEntity(Mockito.any())).thenReturn(
            new TenantEntity());

        when(tenant.checkIfValidEmail()).thenReturn(true);

        tenantManager.update(UUID.randomUUID(), tenant);
        verify(tenantEventPublisher,
            times(1)).publishTenantUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenant_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(tenantMapper.toEntity(Mockito.any())).thenReturn(
            new TenantEntity());

        when(tenantRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantEntity()));

        tenantManager.updatePartial(UUID.randomUUID(), tenant);
        verify(tenantEventPublisher,
            times(1)).publishTenantUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenant_whenDelete_shouldTriggerDeleteEvent() {
        when(tenantMapper.toEntity(Mockito.any())).thenReturn(
            new TenantEntity());

        when(tenantRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantEntity()));

        tenantManager.delete(UUID.randomUUID());
        verify(tenantEventPublisher,
            times(1)).publishTenantDeletedEvent(Mockito.any());
    }
}
