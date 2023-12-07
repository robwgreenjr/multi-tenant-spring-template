package template.authentication.listeners;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.services.ResetPasswordTokenManager;
import template.internal.events.InternalUserCreatedEvent;

@Component
public class CreateInternalUserListener
    implements ApplicationListener<InternalUserCreatedEvent> {
    private final ResetPasswordTokenManager<InternalResetPasswordToken>
        resetPasswordTokenManager;

    public CreateInternalUserListener(
        @Qualifier("InternalResetPasswordTokenManager")
        ResetPasswordTokenManager<InternalResetPasswordToken> resetPasswordTokenManager) {
        this.resetPasswordTokenManager = resetPasswordTokenManager;
    }

    @Override
    public void onApplicationEvent(InternalUserCreatedEvent event) {
        InternalResetPasswordToken
            resetPasswordTokenModel = new InternalResetPasswordToken();
        resetPasswordTokenModel.setUser(event.getUser());
        resetPasswordTokenManager.create(resetPasswordTokenModel);
    }
}
