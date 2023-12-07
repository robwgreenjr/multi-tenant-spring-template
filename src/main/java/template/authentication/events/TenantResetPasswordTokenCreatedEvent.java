package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.TenantResetPasswordToken;

public class TenantResetPasswordTokenCreatedEvent extends ApplicationEvent {
    private final TenantResetPasswordToken resetPasswordToken;

    public TenantResetPasswordTokenCreatedEvent(Object source,
                                                TenantResetPasswordToken resetPasswordToken) {
        super(source);
        this.resetPasswordToken = resetPasswordToken;
    }

    public TenantResetPasswordToken getResetPasswordToken() {
        return resetPasswordToken;
    }
}