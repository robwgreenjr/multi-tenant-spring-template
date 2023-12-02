package template.authentication.listeners;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import template.authentication.events.InternalResetPasswordTokenCreatedEvent;
import template.authentication.helpers.AuthenticationEmailProvider;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.UserPasswordManager;

@Component
public class CreateInternalResetPasswordTokenListener
    implements ApplicationListener<InternalResetPasswordTokenCreatedEvent> {
    private final AuthenticationEmailProvider authenticationEmailProvider;
    private final UserPasswordManager<InternalUserPassword> userPasswordManager;

    public CreateInternalResetPasswordTokenListener(
        AuthenticationEmailProvider authenticationEmailProvider,
        @Qualifier("InternalUserPasswordManager")
        UserPasswordManager<InternalUserPassword> userPasswordManager) {
        this.authenticationEmailProvider = authenticationEmailProvider;
        this.userPasswordManager = userPasswordManager;
    }


    @Override
    public void onApplicationEvent(
        InternalResetPasswordTokenCreatedEvent event) {
        InternalResetPasswordToken resetPasswordTokenModel =
            event.getResetPasswordTokenModel();

        InternalUserPassword userPasswordModel = null;
        try {
//            userPasswordModel = userPasswordManager.findByUserEmail(
//                resetPasswordTokenModel.getUser().getEmail());
        } catch (Exception exception) {
            // when user password isn't found then we do nothing.
        }

        if (userPasswordModel == null ||
            userPasswordModel.getPassword() == null) {
            authenticationEmailProvider.sendCreatePasswordEmail(
                event.getResetPasswordTokenModel());
        } else {
            authenticationEmailProvider.sendForgotPasswordEmail(
                event.getResetPasswordTokenModel());
        }
    }
}
