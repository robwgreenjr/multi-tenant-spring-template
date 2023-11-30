package template.authorization.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.authorization.events.InternalPermissionCreatedEvent;
import template.authorization.events.InternalPermissionDeletedEvent;
import template.authorization.events.InternalPermissionUpdatedEvent;
import template.authorization.models.InternalPermission;

@Service
public class InternalPermissionEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public InternalPermissionEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishInternalPermissionCreatedEvent(
        InternalPermission permission) {
        InternalPermissionCreatedEvent permissionCreatedEvent =
            new InternalPermissionCreatedEvent(this, permission);

        applicationEventPublisher.publishEvent(permissionCreatedEvent);
    }

    public void publishInternalPermissionUpdatedEvent(
        InternalPermission permission) {
        InternalPermissionUpdatedEvent tenantUpdatedEvent =
            new InternalPermissionUpdatedEvent(this, permission);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishInternalPermissionDeletedEvent(
        InternalPermission permission) {
        InternalPermissionDeletedEvent tenantDeletedEvent =
            new InternalPermissionDeletedEvent(this, permission);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}