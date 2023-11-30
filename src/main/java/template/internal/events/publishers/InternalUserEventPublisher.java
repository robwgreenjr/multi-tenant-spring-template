package template.internal.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.internal.events.InternalUserCreatedEvent;
import template.internal.events.InternalUserDeletedEvent;
import template.internal.events.InternalUserUpdatedEvent;
import template.internal.models.InternalUser;

@Service
public class InternalUserEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public InternalUserEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishInternalUserCreatedEvent(InternalUser user) {
        InternalUserCreatedEvent userCreatedEvent =
            new InternalUserCreatedEvent(this, user);

        applicationEventPublisher.publishEvent(userCreatedEvent);
    }

    public void publishInternalUserUpdatedEvent(InternalUser user) {
        InternalUserUpdatedEvent tenantUpdatedEvent =
            new InternalUserUpdatedEvent(this, user);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishInternalUserDeletedEvent(InternalUser user) {
        InternalUserDeletedEvent tenantDeletedEvent =
            new InternalUserDeletedEvent(this, user);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}