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
        InternalResetPasswordToken resetPasswordTokenModel) {
        InternalResetPasswordTokenCreatedEvent resetPasswordTokenCreatedEvent =
            new InternalResetPasswordTokenCreatedEvent(this,
                resetPasswordTokenModel);

        applicationEventPublisher.publishEvent(resetPasswordTokenCreatedEvent);
    }

    public void publishResetPasswordTokenDeletedEvent(
        InternalResetPasswordToken resetPasswordTokenModel) {
        InternalResetPasswordTokenDeletedEvent resetPasswordTokenDeletedEvent =
            new InternalResetPasswordTokenDeletedEvent(this,
                resetPasswordTokenModel);

        applicationEventPublisher.publishEvent(resetPasswordTokenDeletedEvent);
    }
}
