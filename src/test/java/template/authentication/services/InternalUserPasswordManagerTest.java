package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.InternalUserPasswordEntity;
import template.authentication.events.publishers.InternalUserPasswordEventPublisher;
import template.authentication.mappers.InternalUserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.repositories.InternalUserPasswordRepository;
import template.internal.entities.InternalUserEntity;
import template.internal.repositories.InternalUserRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class InternalUserPasswordManagerTest {
    private final InternalUserPasswordRepository userPasswordRepository =
        Mockito.mock(InternalUserPasswordRepository.class);
    private final InternalUserRepository userRepository =
        Mockito.mock(InternalUserRepository.class);
    private final InternalUserPasswordMapper userPasswordMapper =
        Mockito.mock(InternalUserPasswordMapper.class);
    private final InternalUserPasswordEventPublisher
        userPasswordEventPublisher =
        Mockito.mock(
            InternalUserPasswordEventPublisher.class);
    private final InternalUserPasswordEntity userPasswordEntity =
        Mockito.mock(InternalUserPasswordEntity.class);
    private InternalUserPasswordManager userPasswordManager;

    @BeforeEach
    void initUseCase() {
        userPasswordManager =
            new InternalUserPasswordManager(userPasswordRepository,
                userPasswordMapper,
                userPasswordEventPublisher, userRepository);
    }

    @Test
    public void givenUser_whenCreated_shouldTriggerCreateEvent() {
        InternalUserPassword userPassword = new InternalUserPassword();
        when(userPasswordEntity.getUser()).thenReturn(new InternalUserEntity());
        when(userPasswordMapper.entityToObject(
            userPasswordEntity)).thenReturn(
            userPassword);
        when(userPasswordMapper.toEntity(userPassword)).thenReturn(
            userPasswordEntity);
        when(userRepository.getById(Mockito.any())).thenReturn(
            Optional.of(new InternalUserEntity()));

        doNothing().when(userPasswordRepository).save(userPasswordEntity);

        userPasswordManager.create(userPassword);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordCreatedEvent(
            userPassword);
    }

    @Test
    public void givenUser_whenUpdated_shouldTriggerUpdateEvent() {
        InternalUserPasswordEntity userPasswordEntity =
            new InternalUserPasswordEntity();
        InternalUserPassword userPassword = new InternalUserPassword();
        when(userPasswordMapper.toEntity(
            userPassword)).thenReturn(
            userPasswordEntity);
        doNothing().when(userPasswordRepository).save(userPasswordEntity);
        when(userPasswordMapper.entityToObject(userPasswordEntity)).thenReturn(
            userPassword);

        userPasswordManager.update(1, userPassword);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordUpdatedEvent(
            userPassword);
    }

    @Test
    public void givenUser_whenUpdatePartial_shouldTriggerUpdateEvent() {
        InternalUserPasswordEntity userPasswordEntity =
            new InternalUserPasswordEntity();
        Optional<InternalUserPasswordEntity> optional =
            Optional.of(userPasswordEntity);
        InternalUserPassword userPassword = new InternalUserPassword();
        when(userPasswordMapper.toEntity(
            userPassword)).thenReturn(
            userPasswordEntity);
        doNothing().when(userPasswordRepository).save(userPasswordEntity);
        when(userPasswordMapper.entityToObject(userPasswordEntity)).thenReturn(
            userPassword);
        when(userPasswordRepository.getById(1)).thenReturn(optional);

        userPasswordManager.updatePartial(1, userPassword);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordUpdatedEvent(
            userPassword);
    }

    @Test
    public void givenUserId_whenDeleted_shouldTriggerDeletedEvent() {
        InternalUserPasswordEntity userPasswordEntity =
            new InternalUserPasswordEntity();
        InternalUserPassword userPassword = new InternalUserPassword();
        doNothing().when(userPasswordRepository).delete(userPasswordEntity);
        when(userPasswordRepository.getById(1)).thenReturn(
            Optional.of(userPasswordEntity));
        when(userPasswordMapper.entityToObject(userPasswordEntity)).thenReturn(
            userPassword);

        userPasswordManager.delete(1);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordDeletedEvent(
            userPassword);
    }
}
