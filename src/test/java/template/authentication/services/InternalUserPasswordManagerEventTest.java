package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.InternalUserPasswordEntity;
import template.authentication.events.publishers.InternalUserPasswordEventPublisher;
import template.authentication.mappers.InternalUserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.repositories.InternalUserPasswordRepository;

import java.util.Optional;

public class InternalUserPasswordManagerEventTest {
    private final InternalUserPasswordRepository userPasswordRepository =
        Mockito.mock(InternalUserPasswordRepository.class);
    private final InternalUserPasswordMapper userPasswordMapper =
        Mockito.mock(InternalUserPasswordMapper.class);
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
        InternalUserPassword userPassword = new InternalUserPassword();
//        InternalUserPasswordEntity userPassword =
//            new InternalUserPasswordEntity();
//        when(userPasswordMapper.entityToObject(
//            userPassword)).thenReturn(
//            userPassword);
//        when(userPasswordMapper.toUserPassword(userPassword)).thenReturn(
//            userPassword);
//
//        doNothing().when(userPasswordRepository).save(userPassword);
//
//        userPasswordManager.create(userPassword);
//
//        verify(userPasswordEventPublisher,
//            times(1)).publishUserPasswordCreatedEvent(
//            userPassword);
    }

    @Test
    public void givenUser_whenUpdated_shouldTriggerUpdateEvent()
        throws Exception {
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
//        InternalUserPassword userPassword = new InternalUserPassword();
//        when(userPasswordMapper.userPasswordToUserPassword(
//            userPassword)).thenReturn(
//            userPassword);
//        doNothing().when(userPasswordRepository).save(userPassword);
//        when(userPasswordMapper.toUserPassword(userPassword)).thenReturn(
//            userPassword);
//
//        userPasswordManager.update(1, userPassword);
//
//        verify(userPasswordEventPublisher,
//            times(1)).publishUserPasswordUpdatedEvent(
//            userPassword);
    }

    @Test
    public void givenUser_whenUpdatePartial_shouldTriggerUpdateEvent() {
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
        Optional<InternalUserPasswordEntity> optional =
            Optional.of(userPassword);
//        InternalUserPassword userPassword = new InternalUserPassword();
//        when(userPasswordMapper.userPasswordToUserPassword(
//            userPassword)).thenReturn(
//            userPassword);
//        doNothing().when(userPasswordRepository).save(userPassword);
//        when(userPasswordMapper.toUserPassword(userPassword)).thenReturn(
//            userPassword);
//        when(userPasswordRepository.getById(1)).thenReturn(optional);
//
//        userPasswordManager.updatePartial(1, userPassword);
//
//        verify(userPasswordEventPublisher,
//            times(1)).publishUserPasswordUpdatedEvent(
//            userPassword);
    }

    @Test
    public void givenUserId_whenDeleted_shouldTriggerDeletedEvent() {
        InternalUserPasswordEntity userPassword =
            new InternalUserPasswordEntity();
//        InternalUserPassword userPassword = new InternalUserPassword();
//        doNothing().when(userPasswordRepository).delete(userPassword);
//        when(userPasswordRepository.getById(1)).thenReturn(
//            Optional.of(userPassword));
//        when(userPasswordMapper.toUserPassword(userPassword)).thenReturn(
//            userPassword);
//
//        userPasswordManager.delete(1);
//
//        verify(userPasswordEventPublisher,
//            times(1)).publishUserPasswordDeletedEvent(
//            userPassword);
    }
}
