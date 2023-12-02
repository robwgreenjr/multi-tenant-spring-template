package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.TenantResetPasswordToken;

public class TenantResetPasswordTokenDeletedEvent extends ApplicationEvent {
    private final TenantResetPasswordToken resetPasswordToken;

    public TenantResetPasswordTokenDeletedEvent(Object source,
                                                TenantResetPasswordToken resetPasswordToken) {
        super(source);
        this.resetPasswordToken = resetPasswordToken;
    }

    public TenantResetPasswordToken getUser() {
        return resetPasswordToken;
    }
}