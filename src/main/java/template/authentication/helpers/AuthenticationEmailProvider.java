package template.authentication.helpers;

public interface AuthenticationEmailProvider<T> {
    void sendForgotPasswordEmail(T resetPasswordToken);

    void sendCreatePasswordEmail(T resetPasswordToken);
}
