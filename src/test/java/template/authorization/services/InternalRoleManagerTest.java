package template.authorization.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authorization.entities.InternalRoleEntity;
import template.authorization.events.publishers.InternalRoleEventPublisher;
import template.authorization.mappers.InternalRoleMapper;
import template.authorization.models.InternalRole;
import template.authorization.repositories.InternalRoleRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class InternalRoleManagerTest {
    private final InternalRoleRepository roleRepository =
        Mockito.mock(InternalRoleRepository.class);
    private final InternalRoleMapper roleMapper =
        Mockito.mock(InternalRoleMapper.class);
    private final InternalRoleEventPublisher roleEventPublisher =
        Mockito.mock(InternalRoleEventPublisher.class);
    private final InternalRole role =
        Mockito.mock(InternalRole.class);

    private InternalRoleManager roleManager;

    @BeforeEach
    void init() {
        roleManager =
            new InternalRoleManagerImpl(roleRepository,
                roleMapper,
                roleEventPublisher);
    }

    @Test
    public void givenInternalRole_whenCreated_shouldTriggerCreateEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new InternalRoleEntity());

        roleManager.create(role);
        verify(roleEventPublisher,
            times(1)).publishInternalRoleCreatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalRole_whenCreatingList_shouldTriggerCreateEvent() {

        when(roleMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new InternalRole())
                .collect(Collectors.toList()));

        roleManager.createAll(Mockito.anyList());
        verify(roleEventPublisher,
            times(10)).publishInternalRoleCreatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalRole_whenUpdate_shouldTriggerUpdateEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new InternalRoleEntity());

        roleManager.update(Mockito.any(), role);
        verify(roleEventPublisher,
            times(1)).publishInternalRoleUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalRole_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new InternalRoleEntity());

        when(roleRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalRoleEntity()));

        roleManager.updatePartial(Mockito.any(), role);
        verify(roleEventPublisher,
            times(1)).publishInternalRoleUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalRole_whenUpdatedList_shouldTriggerUpdateEvent() {

        when(roleMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new InternalRole())
                .collect(Collectors.toList()));

        roleManager.updateAll(Mockito.anyList());
        verify(roleEventPublisher,
            times(10)).publishInternalRoleUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalRole_whenDelete_shouldTriggerDeleteEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new InternalRoleEntity());

        when(roleRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalRoleEntity()));

        roleManager.delete(Mockito.any());
        verify(roleEventPublisher,
            times(1)).publishInternalRoleDeletedEvent(Mockito.any());
    }
}
