package template.authorization.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.authorization.events.TenantRoleCreatedEvent;
import template.authorization.events.TenantRoleDeletedEvent;
import template.authorization.events.TenantRoleUpdatedEvent;
import template.authorization.models.TenantRole;

@Service
public class TenantRoleEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantRoleEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTenantRoleCreatedEvent(TenantRole role) {
        TenantRoleCreatedEvent roleCreatedEvent =
            new TenantRoleCreatedEvent(this, role);

        applicationEventPublisher.publishEvent(roleCreatedEvent);
    }

    public void publishTenantRoleUpdatedEvent(TenantRole role) {
        TenantRoleUpdatedEvent tenantUpdatedEvent =
            new TenantRoleUpdatedEvent(this, role);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishTenantRoleDeletedEvent(TenantRole role) {
        TenantRoleDeletedEvent tenantDeletedEvent =
            new TenantRoleDeletedEvent(this, role);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}