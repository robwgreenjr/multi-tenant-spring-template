package template.authorization.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authorization.entities.TenantRoleEntity;
import template.authorization.events.publishers.TenantRoleEventPublisher;
import template.authorization.mappers.TenantRoleMapper;
import template.authorization.models.TenantRole;
import template.authorization.repositories.TenantRoleRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class TenantRoleManagerTest {
    private final TenantRoleRepository roleRepository =
        Mockito.mock(TenantRoleRepository.class);
    private final TenantRoleMapper roleMapper =
        Mockito.mock(TenantRoleMapper.class);
    private final TenantRoleEventPublisher roleEventPublisher =
        Mockito.mock(TenantRoleEventPublisher.class);
    private final TenantRole role =
        Mockito.mock(TenantRole.class);

    private TenantRoleManager roleManager;

    @BeforeEach
    void init() {
        roleManager =
            new TenantRoleManagerImpl(roleRepository,
                roleMapper,
                roleEventPublisher);
    }

    @Test
    public void givenTenantRole_whenCreated_shouldTriggerCreateEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new TenantRoleEntity());

        roleManager.create(role);
        verify(roleEventPublisher,
            times(1)).publishTenantRoleCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantRole_whenCreatingList_shouldTriggerCreateEvent() {

        when(roleMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new TenantRole())
                .collect(Collectors.toList()));

        roleManager.createAll(Mockito.anyList());
        verify(roleEventPublisher,
            times(10)).publishTenantRoleCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantRole_whenUpdate_shouldTriggerUpdateEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new TenantRoleEntity());

        roleManager.update(Mockito.any(), role);
        verify(roleEventPublisher,
            times(1)).publishTenantRoleUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantRole_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new TenantRoleEntity());

        when(roleRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantRoleEntity()));

        roleManager.updatePartial(Mockito.any(), role);
        verify(roleEventPublisher,
            times(1)).publishTenantRoleUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantRole_whenUpdatedList_shouldTriggerUpdateEvent() {

        when(roleMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new TenantRole())
                .collect(Collectors.toList()));

        roleManager.updateAll(Mockito.anyList());
        verify(roleEventPublisher,
            times(10)).publishTenantRoleUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantRole_whenDelete_shouldTriggerDeleteEvent() {
        when(roleMapper.toEntity(Mockito.any())).thenReturn(
            new TenantRoleEntity());

        when(roleRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantRoleEntity()));

        roleManager.delete(Mockito.any());
        verify(roleEventPublisher,
            times(1)).publishTenantRoleDeletedEvent(Mockito.any());
    }
}
