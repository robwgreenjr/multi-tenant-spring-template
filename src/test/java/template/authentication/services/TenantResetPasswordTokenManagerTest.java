package template.authentication.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import template.authentication.entities.TenantResetPasswordTokenEntity;
import template.authentication.events.publishers.TenantResetPasswordTokenEventPublisher;
import template.authentication.mappers.TenantResetPasswordTokenMapper;
import template.authentication.models.TenantResetPasswordToken;
import template.authentication.repositories.TenantResetPasswordTokenRepository;

import java.util.Optional;

import static org.mockito.Mockito.*;

public class TenantResetPasswordTokenManagerTest {
    private final TenantResetPasswordTokenRepository
        resetPasswordTokenRepository =
        Mockito.mock(TenantResetPasswordTokenRepository.class);
    private final TenantResetPasswordTokenMapper resetPasswordTokenMapper =
        Mockito.mock(TenantResetPasswordTokenMapper.class);
    private final TenantResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher = Mockito.mock(
        TenantResetPasswordTokenEventPublisher.class);

    private TenantResetPasswordTokenManager resetPasswordTokenManager;

    @BeforeEach
    void initUseCase() {
        resetPasswordTokenManager =
            new TenantResetPasswordTokenManager(
                resetPasswordTokenRepository,
                resetPasswordTokenMapper,
                resetPasswordTokenEventPublisher);
    }

    @Test
    public void whenCreated_shouldTriggerCreateEvent() {
        TenantResetPasswordToken resetPasswordToken =
            new TenantResetPasswordToken();
        TenantResetPasswordTokenEntity resetPasswordTokenEntity =
            new TenantResetPasswordTokenEntity();
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
        TenantResetPasswordTokenEntity resetPasswordTokenEntity =
            new TenantResetPasswordTokenEntity();
        TenantResetPasswordToken resetPasswordToken =
            new TenantResetPasswordToken();

        when(resetPasswordTokenMapper.entityToObject(
            resetPasswordTokenEntity)).thenReturn(resetPasswordToken);

        when(resetPasswordTokenRepository.getByToken(Mockito.any())).thenReturn(
            Optional.of(resetPasswordTokenEntity));

        resetPasswordTokenManager.delete(Mockito.any());

        verify(resetPasswordTokenEventPublisher,
            times(1)).publishResetPasswordTokenDeletedEvent(
            resetPasswordToken);
    }
}
