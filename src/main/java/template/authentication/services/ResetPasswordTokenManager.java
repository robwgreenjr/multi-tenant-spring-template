package template.authentication.services;

import template.authentication.models.InternalResetPasswordToken;

import java.util.UUID;

public interface ResetPasswordTokenManager {
    InternalResetPasswordToken findByUserEmail(String email);

    InternalResetPasswordToken findByToken(UUID token);

    InternalResetPasswordToken create(
        InternalResetPasswordToken resetPasswordTokenModel);

    void delete(UUID token);
}
