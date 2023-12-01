package template.authentication.repositories;

import org.springframework.stereotype.Service;
import template.authentication.entities.InternalResetPasswordTokenEntity;
import template.database.exceptions.NotNullColumnDataException;

import java.util.Optional;
import java.util.UUID;

@Service
public class InternalResetPasswordTokenRepositoryImpl
    implements InternalResetPasswordTokenRepository {
    private final IInternalResetPasswordTokenRepository
        resetPasswordTokenRepository;

    public InternalResetPasswordTokenRepositoryImpl(
        IInternalResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Override
    public Optional<InternalResetPasswordTokenEntity> getByToken(UUID token) {
        return resetPasswordTokenRepository.getByToken(token);
    }

    @Override
    public Optional<InternalResetPasswordTokenEntity> getByUserEmail(
        String email) {
        return resetPasswordTokenRepository.getByUserEmail(email);
    }

    @Override
    public void save(InternalResetPasswordTokenEntity resetPasswordToken) {
        String email;
        try {
            email = resetPasswordToken.getUser().getEmail();
        } catch (Exception exception) {
            throw new NotNullColumnDataException("You must provide a user.");
        }

        Optional<InternalResetPasswordTokenEntity> existingEntity =
            getByUserEmail(email);

        existingEntity.ifPresent(this::delete);

        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public void delete(InternalResetPasswordTokenEntity resetPasswordToken) {
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }
}
