package template.internal.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.internal.entities.InternalUserEntity;
import template.internal.events.publishers.InternalUserEventPublisher;
import template.internal.mappers.InternalUserMapper;
import template.internal.models.InternalUser;
import template.internal.repositories.InternalUserRepository;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.mockito.Mockito.*;

public class InternalUserManagerTest {
    private final InternalUserRepository userRepository =
        Mockito.mock(InternalUserRepository.class);
    private final InternalUserMapper userMapper =
        Mockito.mock(InternalUserMapper.class);
    private final InternalUserEventPublisher userEventPublisher =
        Mockito.mock(InternalUserEventPublisher.class);
    private final InternalUser user = Mockito.mock(InternalUser.class);

    private InternalUserManager userManager;

    @BeforeEach
    void init() {
        userManager =
            new InternalUserManagerImpl(userRepository, userMapper,
                userEventPublisher);
    }

    @Test
    public void givenInternalUser_whenCreated_shouldTriggerCreateEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new InternalUserEntity());

        userManager.create(user);
        verify(userEventPublisher,
            times(1)).publishInternalUserCreatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalUser_whenCreatingList_shouldTriggerCreateEvent() {

        when(userMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new InternalUser())
                .collect(Collectors.toList()));

        userManager.createAll(Mockito.anyList());
        verify(userEventPublisher,
            times(10)).publishInternalUserCreatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalUser_whenUpdate_shouldTriggerUpdateEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new InternalUserEntity());

        userManager.update(Mockito.any(), user);
        verify(userEventPublisher,
            times(1)).publishInternalUserUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalUser_whenUpdatePartial_shouldTriggerUpdateEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new InternalUserEntity());

        when(userRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalUserEntity()));

        userManager.updatePartial(Mockito.any(), user);
        verify(userEventPublisher,
            times(1)).publishInternalUserUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalUser_whenUpdatedList_shouldTriggerUpdateEvent() {

        when(userMapper.entityToList(Mockito.anyList())).thenReturn(
            IntStream.range(0, 10)
                .mapToObj(i -> new InternalUser())
                .collect(Collectors.toList()));

        userManager.updateAll(Mockito.anyList());
        verify(userEventPublisher,
            times(10)).publishInternalUserUpdatedEvent(Mockito.any());
    }

    @Test
    public void givenInternalUser_whenDelete_shouldTriggerDeleteEvent() {
        when(userMapper.toEntity(Mockito.any())).thenReturn(
            new InternalUserEntity());

        when(userRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalUserEntity()));

        userManager.delete(Mockito.any());
        verify(userEventPublisher,
            times(1)).publishInternalUserDeletedEvent(Mockito.any());
    }
}
