package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.InternalResetPasswordTokenEntity;
import template.authentication.events.publishers.InternalResetPasswordTokenEventPublisher;
import template.authentication.mappers.ResetPasswordTokenMapper;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.repositories.InternalResetPasswordTokenRepository;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;

public class ResetPasswordTokenManagerEventTest {
    private final InternalResetPasswordTokenRepository
        resetPasswordTokenRepository =
        Mockito.mock(InternalResetPasswordTokenRepository.class);
    private final ResetPasswordTokenMapper resetPasswordTokenMapper =
        Mockito.mock(ResetPasswordTokenMapper.class);
    private final InternalResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher = Mockito.mock(
        InternalResetPasswordTokenEventPublisher.class);

    private InternalResetPasswordTokenManagerImpl resetPasswordTokenManager;

    @BeforeEach
    void initUseCase() {
        resetPasswordTokenManager =
            new InternalResetPasswordTokenManagerImpl(
                resetPasswordTokenRepository,
                resetPasswordTokenMapper,
                resetPasswordTokenEventPublisher);
    }

    @Test
    public void givenUser_whenCreated_shouldTriggerCreateEvent() {
        InternalResetPasswordToken resetPasswordTokenModel =
            new InternalResetPasswordToken();
        InternalResetPasswordTokenEntity resetPasswordToken =
            new InternalResetPasswordTokenEntity();
        when(
            resetPasswordTokenMapper.resetPasswordTokenModelToResetPasswordToken(
                resetPasswordTokenModel)).thenReturn(
            resetPasswordToken);
        when(resetPasswordTokenMapper.toResetPasswordTokenModel(
            resetPasswordToken)).thenReturn(
            resetPasswordTokenModel);

        doNothing().when(resetPasswordTokenRepository).save(resetPasswordToken);

        resetPasswordTokenManager.create(resetPasswordTokenModel);

        verify(resetPasswordTokenEventPublisher,
            times(1)).publishResetPasswordTokenCreatedEvent(
            resetPasswordTokenModel);
    }

    @Test
    public void givenUserId_whenDeleted_shouldTriggerDeletedEvent() {
        UUID uuid = UUID.randomUUID();

        InternalResetPasswordTokenEntity resetPasswordToken =
            new InternalResetPasswordTokenEntity();
        InternalResetPasswordToken resetPasswordTokenModel =
            new InternalResetPasswordToken();
        doNothing().when(resetPasswordTokenRepository)
            .delete(resetPasswordToken);
        when(resetPasswordTokenRepository.getByToken(uuid)).thenReturn(
            Optional.of(resetPasswordToken));
        when(resetPasswordTokenMapper.toResetPasswordTokenModel(
            resetPasswordToken)).thenReturn(
            resetPasswordTokenModel);

        resetPasswordTokenManager.delete(uuid);

        verify(resetPasswordTokenEventPublisher,
            times(1)).publishResetPasswordTokenDeletedEvent(
            resetPasswordTokenModel);
    }
}
