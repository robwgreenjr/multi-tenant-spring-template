package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.InternalResetPasswordTokenEntity;
import template.authentication.events.publishers.InternalResetPasswordTokenEventPublisher;
import template.authentication.exceptions.ResetPasswordTokenCreateIncompleteException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.mappers.ResetPasswordTokenMapper;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.repositories.InternalResetPasswordTokenRepository;
import template.database.exceptions.NotNullColumnDataException;
import template.global.exceptions.UnknownServerException;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class InternalResetPasswordTokenManager
    implements ResetPasswordTokenManager<InternalResetPasswordToken> {
    private final InternalResetPasswordTokenRepository
        resetPasswordTokenRepository;
    private final ResetPasswordTokenMapper resetPasswordTokenMapper;
    private final InternalResetPasswordTokenEventPublisher
        resetPasswordTokenEventPublisher;

    public InternalResetPasswordTokenManager(
        InternalResetPasswordTokenRepository resetPasswordTokenRepository,
        ResetPasswordTokenMapper resetPasswordTokenMapper,
        InternalResetPasswordTokenEventPublisher resetPasswordTokenEventPublisher) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
        this.resetPasswordTokenMapper = resetPasswordTokenMapper;
        this.resetPasswordTokenEventPublisher =
            resetPasswordTokenEventPublisher;
    }

    @Override
    public InternalResetPasswordToken findByUserEmail(String email) {
        Optional<InternalResetPasswordTokenEntity> resetPasswordToken =
            resetPasswordTokenRepository.getByUserEmail(email);

        if (resetPasswordToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        return resetPasswordTokenMapper.toResetPasswordTokenModel(
            resetPasswordToken.get());
    }

    @Override
    public InternalResetPasswordToken findByToken(UUID token) {
        Optional<InternalResetPasswordTokenEntity> resetPasswordToken =
            resetPasswordTokenRepository.getByToken(token);

        if (resetPasswordToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        return resetPasswordTokenMapper.toResetPasswordTokenModel(
            resetPasswordToken.get());
    }

    @Override
    public InternalResetPasswordToken create(
        InternalResetPasswordToken resetPasswordTokenModel) {
        InternalResetPasswordTokenEntity newResetPasswordToken =
            resetPasswordTokenMapper.resetPasswordTokenModelToResetPasswordToken(
                resetPasswordTokenModel);

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

        resetPasswordTokenModel =
            resetPasswordTokenMapper.toResetPasswordTokenModel(
                newResetPasswordToken);
        resetPasswordTokenEventPublisher.publishResetPasswordTokenCreatedEvent(
            resetPasswordTokenModel);

        return resetPasswordTokenModel;
    }

    @Override
    public void delete(UUID token) {
        Optional<InternalResetPasswordTokenEntity> findEntity =
            resetPasswordTokenRepository.getByToken(token);

        if (findEntity.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        resetPasswordTokenRepository.delete(findEntity.get());

        InternalResetPasswordToken resetPasswordTokenModel =
            resetPasswordTokenMapper.toResetPasswordTokenModel(
                findEntity.get());
        resetPasswordTokenEventPublisher.publishResetPasswordTokenDeletedEvent(
            resetPasswordTokenModel);
    }
}
