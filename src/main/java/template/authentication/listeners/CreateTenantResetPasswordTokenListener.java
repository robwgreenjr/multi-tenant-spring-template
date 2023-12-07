package template.authentication.listeners;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import template.authentication.events.TenantResetPasswordTokenCreatedEvent;
import template.authentication.helpers.AuthenticationEmailProvider;
import template.authentication.models.TenantResetPasswordToken;
import template.authentication.models.TenantUserPassword;
import template.authentication.services.UserPasswordManager;

import java.util.Optional;

@Component
public class CreateTenantResetPasswordTokenListener
    implements ApplicationListener<TenantResetPasswordTokenCreatedEvent> {
    private final AuthenticationEmailProvider<TenantResetPasswordToken>
        authenticationEmailProvider;
    private final UserPasswordManager<TenantUserPassword> userPasswordManager;

    public CreateTenantResetPasswordTokenListener(
        @Qualifier("TenantAuthenticationEmailProvider")
        AuthenticationEmailProvider<TenantResetPasswordToken> authenticationEmailProvider,
        @Qualifier("TenantUserPasswordManager")
        UserPasswordManager<TenantUserPassword> userPasswordManager) {
        this.authenticationEmailProvider = authenticationEmailProvider;
        this.userPasswordManager = userPasswordManager;
    }


    @Override
    public void onApplicationEvent(
        TenantResetPasswordTokenCreatedEvent event) {
        TenantResetPasswordToken resetPasswordToken =
            event.getResetPasswordToken();

        Optional<TenantUserPassword> userPassword = Optional.empty();
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
