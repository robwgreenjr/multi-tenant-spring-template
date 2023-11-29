package template.authentication.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import template.authentication.events.InternalUserPasswordCreatedEvent;
import template.authentication.events.InternalUserPasswordDeletedEvent;
import template.authentication.events.InternalUserPasswordUpdatedEvent;
import template.authentication.models.InternalUserPassword;

@Component
public class InternalUserPasswordEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public InternalUserPasswordEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishUserPasswordCreatedEvent(
        InternalUserPassword userPasswordModel) {
        InternalUserPasswordCreatedEvent userPasswordCreatedEvent =
            new InternalUserPasswordCreatedEvent(this, userPasswordModel);

        applicationEventPublisher.publishEvent(userPasswordCreatedEvent);
    }

    public void publishUserPasswordUpdatedEvent(
        InternalUserPassword userPasswordModel) {
        InternalUserPasswordUpdatedEvent userPasswordUpdatedEvent =
            new InternalUserPasswordUpdatedEvent(this, userPasswordModel);

        applicationEventPublisher.publishEvent(userPasswordUpdatedEvent);
    }

    public void publishUserPasswordDeletedEvent(
        InternalUserPassword userPasswordModel) {
        InternalUserPasswordDeletedEvent userPasswordDeletedEvent =
            new InternalUserPasswordDeletedEvent(this, userPasswordModel);

        applicationEventPublisher.publishEvent(userPasswordDeletedEvent);
    }
}