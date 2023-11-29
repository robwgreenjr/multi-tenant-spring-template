package template.tenants.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.tenants.entities.TenantEmailConfirmationEntity;
import template.tenants.events.publishers.TenantEmailConfirmationEventPublisher;
import template.tenants.mappers.TenantEmailConfirmationMapper;
import template.tenants.models.TenantEmailConfirmation;
import template.tenants.repositories.TenantEmailConfirmationRepository;

import static org.mockito.Mockito.*;

public class TenantEmailConfirmationManagerTest {
    private final TenantEmailConfirmationRepository
        tenantEmailConfirmationRepository =
        Mockito.mock(TenantEmailConfirmationRepository.class);
    private final TenantEmailConfirmationMapper tenantEmailConfirmationMapper =
        Mockito.mock(TenantEmailConfirmationMapper.class);
    private final TenantEmailConfirmationEventPublisher
        tenantEmailConfirmationEventPublisher =
        Mockito.mock(TenantEmailConfirmationEventPublisher.class);

    private TenantEmailConfirmationManager tenantEmailConfirmationManager;

    @BeforeEach
    void init() {
        tenantEmailConfirmationManager =
            new TenantEmailConfirmationManagerImpl(
                tenantEmailConfirmationMapper,
                tenantEmailConfirmationRepository,
                tenantEmailConfirmationEventPublisher);
    }

    @Test
    public void givenTenantEmailConfirmation_whenCreated_shouldTriggerCreateEvent() {
        when(tenantEmailConfirmationMapper.toEntity(Mockito.any())).thenReturn(
            new TenantEmailConfirmationEntity());

        tenantEmailConfirmationManager.create(new TenantEmailConfirmation());
        verify(tenantEmailConfirmationEventPublisher,
            times(1)).publishTenantEmailConfirmationCreatedEvent(Mockito.any());
    }
}
