package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.InternalResetPasswordToken;

public class InternalResetPasswordTokenDeletedEvent extends ApplicationEvent {
    private final InternalResetPasswordToken resetPasswordTokenModel;

    public InternalResetPasswordTokenDeletedEvent(Object source,
                                                  InternalResetPasswordToken resetPasswordTokenModel) {
        super(source);
        this.resetPasswordTokenModel = resetPasswordTokenModel;
    }

    public InternalResetPasswordToken getUser() {
        return resetPasswordTokenModel;
    }
}