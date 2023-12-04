package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.InternalResetPasswordTokenEntity;
import template.authentication.events.publishers.InternalResetPasswordTokenEventPublisher;
import template.authentication.mappers.InternalResetPasswordTokenMapper;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.repositories.InternalResetPasswordTokenRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class InternalResetPasswordTokenManagerTest {
    private final InternalResetPasswordTokenRepository
        resetPasswordTokenRepository =
        Mockito.mock(InternalResetPasswordTokenRepository.class);
    private final InternalResetPasswordTokenMapper resetPasswordTokenMapper =
        Mockito.mock(InternalResetPasswordTokenMapper.class);
    private final InternalResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher = Mockito.mock(
        InternalResetPasswordTokenEventPublisher.class);

    private InternalResetPasswordTokenManager resetPasswordTokenManager;

    @BeforeEach
    void initUseCase() {
        resetPasswordTokenManager =
            new InternalResetPasswordTokenManager(
                resetPasswordTokenRepository,
                resetPasswordTokenMapper,
                resetPasswordTokenEventPublisher);
    }

    @Test
    public void whenCreated_shouldTriggerCreateEvent() {
        InternalResetPasswordToken resetPasswordToken =
            new InternalResetPasswordToken();
        InternalResetPasswordTokenEntity resetPasswordTokenEntity =
            new InternalResetPasswordTokenEntity();
        when(
            resetPasswordTokenMapper.toEntity(
                resetPasswordToken)).thenReturn(
            resetPasswordTokenEntity);
        when(resetPasswordTokenMapper.entityToObject(
            resetPasswordTokenEntity)).thenReturn(
            resetPasswordToken);

        doNothing().when(resetPasswordTokenRepository)
            .save(resetPasswordTokenEntity);

        resetPasswordTokenManager.create(resetPasswordToken);

        verify(resetPasswordTokenEventPublisher,
            times(1)).publishResetPasswordTokenCreatedEvent(
            resetPasswordToken);
    }

    @Test
    public void whenDeleted_shouldTriggerDeletedEvent() {
        InternalResetPasswordTokenEntity resetPasswordTokenEntity =
            new InternalResetPasswordTokenEntity();
        InternalResetPasswordToken resetPasswordToken =
            new InternalResetPasswordToken();

        when(resetPasswordTokenMapper.entityToObject(
            resetPasswordTokenEntity)).thenReturn(
            resetPasswordToken);

        when(resetPasswordTokenRepository.getByToken(Mockito.any())).thenReturn(
            Optional.of(resetPasswordTokenEntity));

        resetPasswordTokenManager.delete(Mockito.any());

        verify(resetPasswordTokenEventPublisher,
            times(1)).publishResetPasswordTokenDeletedEvent(
            resetPasswordToken);
    }
}
