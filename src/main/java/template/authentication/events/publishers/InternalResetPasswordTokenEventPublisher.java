package template.authentication.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import template.authentication.events.InternalResetPasswordTokenCreatedEvent;
import template.authentication.events.InternalResetPasswordTokenDeletedEvent;
import template.authentication.models.InternalResetPasswordToken;

@Component
public class InternalResetPasswordTokenEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public InternalResetPasswordTokenEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishResetPasswordTokenCreatedEvent(
        InternalResetPasswordToken resetPasswordToken) {
        InternalResetPasswordTokenCreatedEvent resetPasswordTokenCreatedEvent =
            new InternalResetPasswordTokenCreatedEvent(this,
                resetPasswordToken);

        applicationEventPublisher.publishEvent(resetPasswordTokenCreatedEvent);
    }

    public void publishResetPasswordTokenDeletedEvent(
        InternalResetPasswordToken resetPasswordToken) {
        InternalResetPasswordTokenDeletedEvent resetPasswordTokenDeletedEvent =
            new InternalResetPasswordTokenDeletedEvent(this,
                resetPasswordToken);

        applicationEventPublisher.publishEvent(resetPasswordTokenDeletedEvent);
    }
}
