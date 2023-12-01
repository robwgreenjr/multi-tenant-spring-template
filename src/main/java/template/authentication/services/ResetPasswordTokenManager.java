package template.authentication.services;

import java.util.UUID;

public interface ResetPasswordTokenManager<T> {
    T findByUserEmail(String email);

    T findByToken(UUID token);

    T create(T resetPasswordToken);

    void delete(UUID token);
}
