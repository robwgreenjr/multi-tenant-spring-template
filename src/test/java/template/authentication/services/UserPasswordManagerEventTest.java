package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.InternalUserPasswordEntity;
import template.authentication.events.publishers.InternalUserPasswordEventPublisher;
import template.authentication.mappers.UserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.repositories.InternalUserPasswordRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class UserPasswordManagerEventTest {
    private final InternalUserPasswordRepository userPasswordRepository =
        Mockito.mock(InternalUserPasswordRepository.class);
    private final UserPasswordMapper userPasswordMapper =
        Mockito.mock(UserPasswordMapper.class);
    private final InternalUserPasswordEventPublisher
        userPasswordEventPublisher =
        Mockito.mock(
            InternalUserPasswordEventPublisher.class);

    private InternalUserPasswordManager userPasswordManager;

    @BeforeEach
    void initUseCase() {
        userPasswordManager =
            new InternalUserPasswordManager(userPasswordRepository,
                userPasswordMapper,
                userPasswordEventPublisher);
    }

    @Test
    public void givenUser_whenCreated_shouldTriggerCreateEvent() {
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
        when(userPasswordMapper.userPasswordModelToUserPassword(
            userPasswordModel)).thenReturn(
            userPassword);
        when(userPasswordMapper.toUserPasswordModel(userPassword)).thenReturn(
            userPasswordModel);

        doNothing().when(userPasswordRepository).save(userPassword);

        userPasswordManager.create(userPasswordModel);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordCreatedEvent(
            userPasswordModel);
    }

    @Test
    public void givenUser_whenUpdated_shouldTriggerUpdateEvent()
        throws Exception {
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        when(userPasswordMapper.userPasswordModelToUserPassword(
            userPasswordModel)).thenReturn(
            userPassword);
        doNothing().when(userPasswordRepository).save(userPassword);
        when(userPasswordMapper.toUserPasswordModel(userPassword)).thenReturn(
            userPasswordModel);

        userPasswordManager.update(1, userPasswordModel);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordUpdatedEvent(
            userPasswordModel);
    }

    @Test
    public void givenUser_whenUpdatePartial_shouldTriggerUpdateEvent() {
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
        Optional<InternalUserPasswordEntity> optional =
            Optional.of(userPassword);
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        when(userPasswordMapper.userPasswordModelToUserPassword(
            userPasswordModel)).thenReturn(
            userPassword);
        doNothing().when(userPasswordRepository).save(userPassword);
        when(userPasswordMapper.toUserPasswordModel(userPassword)).thenReturn(
            userPasswordModel);
        when(userPasswordRepository.getById(1)).thenReturn(optional);

        userPasswordManager.updatePartial(1, userPasswordModel);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordUpdatedEvent(
            userPasswordModel);
    }

    @Test
    public void givenUserId_whenDeleted_shouldTriggerDeletedEvent() {
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        doNothing().when(userPasswordRepository).delete(userPassword);
        when(userPasswordRepository.getById(1)).thenReturn(
            Optional.of(userPassword));
        when(userPasswordMapper.toUserPasswordModel(userPassword)).thenReturn(
            userPasswordModel);

        userPasswordManager.delete(1);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordDeletedEvent(
            userPasswordModel);
    }
}
