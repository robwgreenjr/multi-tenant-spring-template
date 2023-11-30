package template.authorization.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.authorization.events.TenantPermissionCreatedEvent;
import template.authorization.events.TenantPermissionDeletedEvent;
import template.authorization.events.TenantPermissionUpdatedEvent;
import template.authorization.models.TenantPermission;

@Service
public class TenantPermissionEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantPermissionEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTenantPermissionCreatedEvent(
        TenantPermission permission) {
        TenantPermissionCreatedEvent permissionCreatedEvent =
            new TenantPermissionCreatedEvent(this, permission);

        applicationEventPublisher.publishEvent(permissionCreatedEvent);
    }

    public void publishTenantPermissionUpdatedEvent(
        TenantPermission permission) {
        TenantPermissionUpdatedEvent tenantUpdatedEvent =
            new TenantPermissionUpdatedEvent(this, permission);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishTenantPermissionDeletedEvent(
        TenantPermission permission) {
        TenantPermissionDeletedEvent tenantDeletedEvent =
            new TenantPermissionDeletedEvent(this, permission);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}