package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.InternalResetPasswordToken;

public class InternalResetPasswordTokenCreatedEvent extends ApplicationEvent {
    private final InternalResetPasswordToken resetPasswordToken;

    public InternalResetPasswordTokenCreatedEvent(Object source,
                                                  InternalResetPasswordToken resetPasswordToken) {
        super(source);
        this.resetPasswordToken = resetPasswordToken;
    }

    public InternalResetPasswordToken getResetPasswordToken() {
        return resetPasswordToken;
    }
}