package template.authentication.repositories;

import template.authentication.entities.TenantResetPasswordTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface TenantResetPasswordTokenRepository {
    Optional<TenantResetPasswordTokenEntity> getByToken(UUID token);

    Optional<TenantResetPasswordTokenEntity> getByUserEmail(String email);

    void save(TenantResetPasswordTokenEntity resetPasswordToken);

    void delete(TenantResetPasswordTokenEntity resetPasswordToken);
}
