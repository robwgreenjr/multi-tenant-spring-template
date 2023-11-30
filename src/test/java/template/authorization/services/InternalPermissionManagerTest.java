package template.authorization.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authorization.entities.InternalPermissionEntity;
import template.authorization.events.publishers.InternalPermissionEventPublisher;
import template.authorization.mappers.InternalPermissionMapper;
import template.authorization.models.InternalPermission;
import template.authorization.repositories.InternalPermissionRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class InternalPermissionManagerTest {
    private final InternalPermissionRepository permissionRepository =
        Mockito.mock(InternalPermissionRepository.class);
    private final InternalPermissionMapper permissionMapper =
        Mockito.mock(InternalPermissionMapper.class);
    private final InternalPermissionEventPublisher permissionEventPublisher =
        Mockito.mock(InternalPermissionEventPublisher.class);
    private final InternalPermission permission =
        Mockito.mock(InternalPermission.class);

    private InternalPermissionManager permissionManager;

    @BeforeEach
    void init() {
        permissionManager =
            new InternalPermissionManagerImpl(permissionRepository,
                permissionMapper,
                permissionEventPublisher);
    }

    @Test
    public void givenInternalPermission_whenCreated_shouldTriggerCreateEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new InternalPermissionEntity());

        permissionManager.create(permission);
        verify(permissionEventPublisher,
            times(1)).publishInternalPermissionCreatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalPermission_whenCreatingList_shouldTriggerCreateEvent() {

        when(permissionMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new InternalPermission())
                .collect(Collectors.toList()));

        permissionManager.createAll(Mockito.anyList());
        verify(permissionEventPublisher,
            times(10)).publishInternalPermissionCreatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalPermission_whenUpdate_shouldTriggerUpdateEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new InternalPermissionEntity());

        permissionManager.update(Mockito.any(), permission);
        verify(permissionEventPublisher,
            times(1)).publishInternalPermissionUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalPermission_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new InternalPermissionEntity());

        when(permissionRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalPermissionEntity()));

        permissionManager.updatePartial(Mockito.any(), permission);
        verify(permissionEventPublisher,
            times(1)).publishInternalPermissionUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalPermission_whenUpdatedList_shouldTriggerUpdateEvent() {

        when(permissionMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new InternalPermission())
                .collect(Collectors.toList()));

        permissionManager.updateAll(Mockito.anyList());
        verify(permissionEventPublisher,
            times(10)).publishInternalPermissionUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalPermission_whenDelete_shouldTriggerDeleteEvent() {
        when(permissionMapper.toEntity(Mockito.any())).thenReturn(
            new InternalPermissionEntity());

        when(permissionRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalPermissionEntity()));

        permissionManager.delete(Mockito.any());
        verify(permissionEventPublisher,
            times(1)).publishInternalPermissionDeletedEvent(Mockito.any());
    }
}
