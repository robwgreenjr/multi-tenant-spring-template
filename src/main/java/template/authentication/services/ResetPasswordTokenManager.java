package template.authentication.services;

import java.util.Optional;
import java.util.UUID;

public interface ResetPasswordTokenManager<T> {
    Optional<T> findByUserEmail(String email);

    Optional<T> findByToken(UUID token);

    T create(T resetPasswordToken);

    void delete(UUID token);
}
