package template.authentication.repositories;

import org.springframework.stereotype.Service;
import template.authentication.entities.TenantResetPasswordTokenEntity;
import template.database.exceptions.NotNullColumnDataException;

import java.util.Optional;
import java.util.UUID;

@Service
public class TenantResetPasswordTokenRepositoryImpl
    implements TenantResetPasswordTokenRepository {
    private final ITenantResetPasswordTokenRepository
        resetPasswordTokenRepository;

    public TenantResetPasswordTokenRepositoryImpl(
        ITenantResetPasswordTokenRepository resetPasswordTokenRepository) {
        this.resetPasswordTokenRepository = resetPasswordTokenRepository;
    }

    @Override
    public Optional<TenantResetPasswordTokenEntity> getByToken(UUID token) {
        return resetPasswordTokenRepository.getByToken(token);
    }

    @Override
    public Optional<TenantResetPasswordTokenEntity> getByUserEmail(
        String email) {
        return resetPasswordTokenRepository.getByUserEmail(email);
    }

    @Override
    public void save(TenantResetPasswordTokenEntity resetPasswordToken) {
        String email;
        try {
            email = resetPasswordToken.getUser().getEmail();
        } catch (Exception exception) {
            throw new NotNullColumnDataException("You must provide a user.");
        }

        Optional<TenantResetPasswordTokenEntity> existingEntity =
            getByUserEmail(email);

        existingEntity.ifPresent(this::delete);

        resetPasswordTokenRepository.save(resetPasswordToken);
    }

    @Override
    public void delete(TenantResetPasswordTokenEntity resetPasswordToken) {
        resetPasswordTokenRepository.delete(resetPasswordToken);
    }
}
