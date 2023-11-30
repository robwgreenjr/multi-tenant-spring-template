package template.authorization.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authorization.entities.TenantPermissionEntity;
import template.authorization.events.publishers.TenantPermissionEventPublisher;
import template.authorization.mappers.TenantPermissionMapper;
import template.authorization.models.TenantPermission;
import template.authorization.repositories.TenantPermissionRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class TenantPermissionManagerTest {
    private final TenantPermissionRepository permissionRepository =
        Mockito.mock(TenantPermissionRepository.class);
    private final TenantPermissionMapper permissionMapper =
        Mockito.mock(TenantPermissionMapper.class);
    private final TenantPermissionEventPublisher permissionEventPublisher =
        Mockito.mock(TenantPermissionEventPublisher.class);
    private final TenantPermission permission =
        Mockito.mock(TenantPermission.class);

    private TenantPermissionManager permissionManager;

    @BeforeEach
    void init() {
        permissionManager =
            new TenantPermissionManagerImpl(permissionRepository,
                permissionMapper,
                permissionEventPublisher);
    }

    @Test
    public void givenTenantPermission_whenCreated_shouldTriggerCreateEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new TenantPermissionEntity());

        permissionManager.create(permission);
        verify(permissionEventPublisher,
            times(1)).publishTenantPermissionCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantPermission_whenCreatingList_shouldTriggerCreateEvent() {

        when(permissionMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new TenantPermission())
                .collect(Collectors.toList()));

        permissionManager.createAll(Mockito.anyList());
        verify(permissionEventPublisher,
            times(10)).publishTenantPermissionCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantPermission_whenUpdate_shouldTriggerUpdateEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new TenantPermissionEntity());

        permissionManager.update(Mockito.any(), permission);
        verify(permissionEventPublisher,
            times(1)).publishTenantPermissionUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantPermission_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new TenantPermissionEntity());

        when(permissionRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantPermissionEntity()));

        permissionManager.updatePartial(Mockito.any(), permission);
        verify(permissionEventPublisher,
            times(1)).publishTenantPermissionUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantPermission_whenUpdatedList_shouldTriggerUpdateEvent() {

        when(permissionMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new TenantPermission())
                .collect(Collectors.toList()));

        permissionManager.updateAll(Mockito.anyList());
        verify(permissionEventPublisher,
            times(10)).publishTenantPermissionUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantPermission_whenDelete_shouldTriggerDeleteEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new TenantPermissionEntity());

        when(permissionRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantPermissionEntity()));

        permissionManager.delete(Mockito.any());
        verify(permissionEventPublisher,
            times(1)).publishTenantPermissionDeletedEvent(Mockito.any());
    }
}
