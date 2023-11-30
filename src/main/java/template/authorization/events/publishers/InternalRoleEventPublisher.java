package template.authorization.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.authorization.events.InternalRoleCreatedEvent;
import template.authorization.events.InternalRoleDeletedEvent;
import template.authorization.events.InternalRoleUpdatedEvent;
import template.authorization.models.InternalRole;

@Service
public class InternalRoleEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public InternalRoleEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishInternalRoleCreatedEvent(InternalRole role) {
        InternalRoleCreatedEvent roleCreatedEvent =
            new InternalRoleCreatedEvent(this, role);

        applicationEventPublisher.publishEvent(roleCreatedEvent);
    }

    public void publishInternalRoleUpdatedEvent(InternalRole role) {
        InternalRoleUpdatedEvent tenantUpdatedEvent =
            new InternalRoleUpdatedEvent(this, role);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishInternalRoleDeletedEvent(InternalRole role) {
        InternalRoleDeletedEvent tenantDeletedEvent =
            new InternalRoleDeletedEvent(this, role);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}