package template.authentication.listeners;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import template.authentication.events.InternalResetPasswordTokenCreatedEvent;
import template.authentication.helpers.AuthenticationEmailProvider;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.UserPasswordManager;

import java.util.Optional;

@Component
public class CreateInternalResetPasswordTokenListener
    implements ApplicationListener<InternalResetPasswordTokenCreatedEvent> {
    private final AuthenticationEmailProvider<InternalResetPasswordToken>
        authenticationEmailProvider;
    private final UserPasswordManager<InternalUserPassword> userPasswordManager;

    public CreateInternalResetPasswordTokenListener(
        @Qualifier("InternalAuthenticationEmailProvider")
        AuthenticationEmailProvider<InternalResetPasswordToken> authenticationEmailProvider,
        @Qualifier("InternalUserPasswordManager")
        UserPasswordManager<InternalUserPassword> userPasswordManager) {
        this.authenticationEmailProvider = authenticationEmailProvider;
        this.userPasswordManager = userPasswordManager;
    }


    @Override
    public void onApplicationEvent(
        InternalResetPasswordTokenCreatedEvent event) {
        InternalResetPasswordToken resetPasswordToken =
            event.getResetPasswordToken();

        Optional<InternalUserPassword> userPassword = Optional.empty();
        try {
            userPassword = userPasswordManager.findByUserEmail(
                resetPasswordToken.getUser().getEmail());
        } catch (Exception exception) {
            // when user password isn't found then we do nothing.
        }

        if (userPassword.isEmpty() ||
            userPassword.get().getPassword() == null) {
            authenticationEmailProvider.sendCreatePasswordEmail(
                event.getResetPasswordToken());
        } else {
            authenticationEmailProvider.sendForgotPasswordEmail(
                event.getResetPasswordToken());
        }
    }
}
