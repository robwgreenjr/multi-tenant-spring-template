package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.InternalResetPasswordTokenEntity;
import template.authentication.events.publishers.InternalResetPasswordTokenEventPublisher;
import template.authentication.exceptions.ResetPasswordTokenCreateIncompleteException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.mappers.InternalResetPasswordTokenMapper;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.repositories.InternalResetPasswordTokenRepository;
import template.database.exceptions.NotNullColumnDataException;
import template.global.exceptions.UnknownServerException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service("InternalResetPasswordTokenManager")
public class InternalResetPasswordTokenManager
    implements ResetPasswordTokenManager<InternalResetPasswordToken> {
    private final InternalResetPasswordTokenRepository
        resetPasswordTokenRepository;
    private final InternalResetPasswordTokenMapper resetPasswordTokenMapper;
    private final InternalResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher;

    public InternalResetPasswordTokenManager(
        InternalResetPasswordTokenRepository resetPasswordTokenRepository,
        InternalResetPasswordTokenMapper resetPasswordTokenMapper,
        InternalResetPasswordTokenEventPublisher resetPasswordTokenEventPublisher) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.resetPasswordTokenMapper = resetPasswordTokenMapper;
        this.resetPasswordTokenEventPublisher =
            resetPasswordTokenEventPublisher;
    }

    @Override
    public Optional<InternalResetPasswordToken> findByUserEmail(String email) {
        Optional<InternalResetPasswordTokenEntity> resetPasswordTokenEntity =
            resetPasswordTokenRepository.getByUserEmail(email);

        if (resetPasswordTokenEntity.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        return Optional.of(resetPasswordTokenMapper.entityToObject(
            resetPasswordTokenEntity.get()));
    }

    @Override
    public Optional<InternalResetPasswordToken> findByToken(UUID token) {
        Optional<InternalResetPasswordTokenEntity> resetPasswordTokenEntity =
            resetPasswordTokenRepository.getByToken(token);

        if (resetPasswordTokenEntity.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        return Optional.of(resetPasswordTokenMapper.entityToObject(
            resetPasswordTokenEntity.get()));
    }

    @Override
    public InternalResetPasswordToken create(
        InternalResetPasswordToken resetPasswordToken) {
        InternalResetPasswordTokenEntity newResetPasswordToken =
            resetPasswordTokenMapper.toEntity(resetPasswordToken);

        newResetPasswordToken.setToken(UUID.randomUUID());

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

        resetPasswordToken =
            resetPasswordTokenMapper.entityToObject(newResetPasswordToken);
        resetPasswordTokenEventPublisher.publishResetPasswordTokenCreatedEvent(
            resetPasswordToken);

        return resetPasswordToken;
    }

    @Override
    public void delete(UUID token) {
        Optional<InternalResetPasswordTokenEntity> findEntity =
            resetPasswordTokenRepository.getByToken(token);

        if (findEntity.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        resetPasswordTokenRepository.delete(findEntity.get());

        InternalResetPasswordToken resetPasswordToken =
            resetPasswordTokenMapper.entityToObject(findEntity.get());
        resetPasswordTokenEventPublisher.publishResetPasswordTokenDeletedEvent(
            resetPasswordToken);
    }
}
