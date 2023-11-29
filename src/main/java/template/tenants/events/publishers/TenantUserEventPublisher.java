package template.tenants.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.tenants.events.TenantUserCreatedEvent;
import template.tenants.events.TenantUserDeletedEvent;
import template.tenants.events.TenantUserUpdatedEvent;
import template.tenants.models.TenantUser;

@Service
public class TenantUserEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantUserEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTenantUserCreatedEvent(TenantUser user) {
        TenantUserCreatedEvent userCreatedEvent =
            new TenantUserCreatedEvent(this, user);

        applicationEventPublisher.publishEvent(userCreatedEvent);
    }

    public void publishTenantUserUpdatedEvent(TenantUser user) {
        TenantUserUpdatedEvent tenantUpdatedEvent =
            new TenantUserUpdatedEvent(this, user);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishTenantUserDeletedEvent(TenantUser user) {
        TenantUserDeletedEvent tenantDeletedEvent =
            new TenantUserDeletedEvent(this, user);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}