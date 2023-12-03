package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.TenantResetPasswordTokenEntity;
import template.authentication.events.publishers.TenantResetPasswordTokenEventPublisher;
import template.authentication.exceptions.ResetPasswordTokenCreateIncompleteException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.mappers.TenantResetPasswordTokenMapper;
import template.authentication.models.TenantResetPasswordToken;
import template.authentication.repositories.TenantResetPasswordTokenRepository;
import template.database.exceptions.NotNullColumnDataException;
import template.global.exceptions.UnknownServerException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service("TenantResetPasswordTokenManager")
public class TenantResetPasswordTokenManager
    implements ResetPasswordTokenManager<TenantResetPasswordToken> {
    private final TenantResetPasswordTokenRepository
        resetPasswordTokenRepository;
    private final TenantResetPasswordTokenMapper resetPasswordTokenMapper;
    private final TenantResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher;

    public TenantResetPasswordTokenManager(
        TenantResetPasswordTokenRepository resetPasswordTokenRepository,
        TenantResetPasswordTokenMapper resetPasswordTokenMapper,
        TenantResetPasswordTokenEventPublisher resetPasswordTokenEventPublisher) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.resetPasswordTokenMapper = resetPasswordTokenMapper;
        this.resetPasswordTokenEventPublisher =
            resetPasswordTokenEventPublisher;
    }

    @Override
    public TenantResetPasswordToken create(
        TenantResetPasswordToken resetPasswordTokenModel) {
        TenantResetPasswordTokenEntity newResetPasswordToken =
            resetPasswordTokenMapper.toEntity(resetPasswordTokenModel);

//        newResetPasswordToken.setToken(UUID.randomUUID());

        try {
            resetPasswordTokenRepository.save(newResetPasswordToken);
        } catch (NotNullColumnDataException exception) {
            if (Objects.requireNonNull(exception.getReason())
                .contains("user")) {
                throw new ResetPasswordTokenCreateIncompleteException(
                    "To create a reset password token a user must be associated with said token.");
            }
        } catch (Exception exception) {
            throw new UnknownServerException(exception.getMessage());
        }

        resetPasswordTokenModel =
            resetPasswordTokenMapper.entityToObject(newResetPasswordToken);
        resetPasswordTokenEventPublisher.publishResetPasswordTokenCreatedEvent(
            resetPasswordTokenModel);

        return resetPasswordTokenModel;
    }

    @Override
    public void delete(UUID token) {
        Optional<TenantResetPasswordTokenEntity> findEntity =
            resetPasswordTokenRepository.getByToken(token);

        if (findEntity.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        resetPasswordTokenRepository.delete(findEntity.get());

        TenantResetPasswordToken resetPasswordTokenModel =
            resetPasswordTokenMapper.entityToObject(
                findEntity.get());
        resetPasswordTokenEventPublisher.publishResetPasswordTokenDeletedEvent(
            resetPasswordTokenModel);
    }

    @Override
    public Optional<TenantResetPasswordToken> findByToken(UUID token) {
        Optional<TenantResetPasswordTokenEntity> resetPasswordToken =
            resetPasswordTokenRepository.getByToken(token);

        if (resetPasswordToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        return Optional.of(resetPasswordTokenMapper.entityToObject(
            resetPasswordToken.get()));
    }

    @Override
    public Optional<TenantResetPasswordToken> findByUserEmail(String email) {
        Optional<TenantResetPasswordTokenEntity> resetPasswordToken =
            resetPasswordTokenRepository.getByUserEmail(email);

        if (resetPasswordToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        return Optional.of(resetPasswordTokenMapper.entityToObject(
            resetPasswordToken.get()));
    }
}
