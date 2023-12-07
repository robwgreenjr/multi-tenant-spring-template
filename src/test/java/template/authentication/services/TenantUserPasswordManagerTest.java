package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.TenantUserPasswordEntity;
import template.authentication.events.publishers.TenantUserPasswordEventPublisher;
import template.authentication.mappers.TenantUserPasswordMapper;
import template.authentication.models.TenantUserPassword;
import template.authentication.repositories.TenantUserPasswordRepository;
import template.tenants.entities.TenantUserEntity;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class TenantUserPasswordManagerTest {
    private final TenantUserPasswordRepository userPasswordRepository =
        Mockito.mock(TenantUserPasswordRepository.class);
    private final TenantUserPasswordMapper userPasswordMapper =
        Mockito.mock(TenantUserPasswordMapper.class);
    private final TenantUserPasswordEventPublisher
        userPasswordEventPublisher =
        Mockito.mock(
            TenantUserPasswordEventPublisher.class);
    private final TenantUserPasswordEntity userPasswordEntity =
        Mockito.mock(TenantUserPasswordEntity.class);
    private TenantUserPasswordManager userPasswordManager;

    @BeforeEach
    void initUseCase() {
        userPasswordManager =
            new TenantUserPasswordManager(userPasswordRepository,
                userPasswordMapper,
                userPasswordEventPublisher);
    }

    @Test
    public void givenUser_whenCreated_shouldTriggerCreateEvent() {
        TenantUserPassword userPassword = new TenantUserPassword();
        when(userPasswordEntity.getUser()).thenReturn(new TenantUserEntity());
        when(userPasswordMapper.entityToObject(
            userPasswordEntity)).thenReturn(
            userPassword);
        when(userPasswordMapper.toEntity(userPassword)).thenReturn(
            userPasswordEntity);

        doNothing().when(userPasswordRepository).save(userPasswordEntity);

        userPasswordManager.create(userPassword);

        verify(userPasswordEventPublisher,
            times(1)).publishUserPasswordCreatedEvent(
            userPassword);
    }

    @Test
    public void givenUser_whenUpdated_shouldTriggerUpdateEvent() {
        TenantUserPasswordEntity userPasswordEntity =
            new TenantUserPasswordEntity();
        TenantUserPassword userPassword = new TenantUserPassword();
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
        TenantUserPasswordEntity userPasswordEntity =
            new TenantUserPasswordEntity();
        Optional<TenantUserPasswordEntity> optional =
            Optional.of(userPasswordEntity);
        TenantUserPassword userPassword = new TenantUserPassword();
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
        TenantUserPasswordEntity userPasswordEntity =
            new TenantUserPasswordEntity();
        TenantUserPassword userPassword = new TenantUserPassword();
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
