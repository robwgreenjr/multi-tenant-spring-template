package template.authentication.repositories;

import template.authentication.entities.InternalResetPasswordTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface InternalResetPasswordTokenRepository {
    Optional<InternalResetPasswordTokenEntity> getByToken(UUID token);

    Optional<InternalResetPasswordTokenEntity> getByUserEmail(String email);

    void save(InternalResetPasswordTokenEntity resetPasswordToken);

    void delete(InternalResetPasswordTokenEntity resetPasswordToken);
}
