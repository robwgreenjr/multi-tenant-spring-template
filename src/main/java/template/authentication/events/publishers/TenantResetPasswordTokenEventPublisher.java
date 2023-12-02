package template.authentication.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import template.authentication.events.TenantResetPasswordTokenCreatedEvent;
import template.authentication.events.TenantResetPasswordTokenDeletedEvent;
import template.authentication.models.TenantResetPasswordToken;

@Component
public class TenantResetPasswordTokenEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantResetPasswordTokenEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishResetPasswordTokenCreatedEvent(
        TenantResetPasswordToken resetPasswordTokenModel) {
        TenantResetPasswordTokenCreatedEvent resetPasswordTokenCreatedEvent =
            new TenantResetPasswordTokenCreatedEvent(this,
                resetPasswordTokenModel);

        applicationEventPublisher.publishEvent(resetPasswordTokenCreatedEvent);
    }

    public void publishResetPasswordTokenDeletedEvent(
        TenantResetPasswordToken resetPasswordTokenModel) {
        TenantResetPasswordTokenDeletedEvent resetPasswordTokenDeletedEvent =
            new TenantResetPasswordTokenDeletedEvent(this,
                resetPasswordTokenModel);

        applicationEventPublisher.publishEvent(resetPasswordTokenDeletedEvent);
    }
}
