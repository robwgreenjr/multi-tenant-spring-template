package template.authentication.helpers;

import template.authentication.models.InternalResetPasswordToken;

public interface AuthenticationEmailProvider {
    void sendForgotPasswordEmail(
        InternalResetPasswordToken resetPasswordTokenModel);

    void sendCreatePasswordEmail(
        InternalResetPasswordToken resetPasswordTokenModel);
}
