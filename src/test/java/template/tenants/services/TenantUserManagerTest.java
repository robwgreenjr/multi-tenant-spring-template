package template.tenants.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.tenants.entities.TenantUserEntity;
import template.tenants.events.publishers.TenantUserEventPublisher;
import template.tenants.mappers.TenantUserMapper;
import template.tenants.models.TenantUser;
import template.tenants.repositories.TenantUserRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class TenantUserManagerTest {
    private final TenantUserRepository userRepository =
        Mockito.mock(TenantUserRepository.class);
    private final TenantUserMapper userMapper =
        Mockito.mock(TenantUserMapper.class);
    private final TenantUserEventPublisher userEventPublisher =
        Mockito.mock(TenantUserEventPublisher.class);
    private final TenantUser user = Mockito.mock(TenantUser.class);

    private TenantUserManager userManager;

    @BeforeEach
    void init() {
        userManager =
            new TenantUserManagerImpl(userRepository, userMapper,
                userEventPublisher);
    }

    @Test
    public void givenTenantUser_whenCreated_shouldTriggerCreateEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new TenantUserEntity());

        userManager.create(user);
        verify(userEventPublisher,
            times(1)).publishTenantUserCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantUser_whenCreatingList_shouldTriggerCreateEvent() {

        when(userMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new TenantUser())
                .collect(Collectors.toList()));

        userManager.createAll(Mockito.anyList());
        verify(userEventPublisher,
            times(10)).publishTenantUserCreatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantUser_whenUpdate_shouldTriggerUpdateEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new TenantUserEntity());

        userManager.update(Mockito.any(), user);
        verify(userEventPublisher,
            times(1)).publishTenantUserUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantUser_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new TenantUserEntity());

        when(userRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantUserEntity()));

        userManager.updatePartial(Mockito.any(), user);
        verify(userEventPublisher,
            times(1)).publishTenantUserUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantUser_whenUpdatedList_shouldTriggerUpdateEvent() {

        when(userMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new TenantUser())
                .collect(Collectors.toList()));

        userManager.updateAll(Mockito.anyList());
        verify(userEventPublisher,
            times(10)).publishTenantUserUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenTenantUser_whenDelete_shouldTriggerDeleteEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new TenantUserEntity());

        when(userRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new TenantUserEntity()));

        userManager.delete(Mockito.any());
        verify(userEventPublisher,
            times(1)).publishTenantUserDeletedEvent(Mockito.any());
    }
}
