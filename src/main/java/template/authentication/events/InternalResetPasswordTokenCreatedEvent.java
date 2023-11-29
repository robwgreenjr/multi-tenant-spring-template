package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.InternalResetPasswordToken;

public class InternalResetPasswordTokenCreatedEvent extends ApplicationEvent {
    private final InternalResetPasswordToken resetPasswordTokenModel;

    public InternalResetPasswordTokenCreatedEvent(Object source,
                                                  InternalResetPasswordToken resetPasswordTokenModel) {
        super(source);
        this.resetPasswordTokenModel = resetPasswordTokenModel;
    }

    public InternalResetPasswordToken getResetPasswordTokenModel() {
        return resetPasswordTokenModel;
    }
}