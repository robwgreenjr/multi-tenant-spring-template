package template.tenants.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.tenants.events.TenantCreatedEvent;
import template.tenants.events.TenantDeletedEvent;
import template.tenants.events.TenantUpdatedEvent;
import template.tenants.models.Tenant;

@Service
public class TenantEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTenantCreatedEvent(Tenant tenant) {
        TenantCreatedEvent tenantCreatedEvent =
            new TenantCreatedEvent(this, tenant);

        applicationEventPublisher.publishEvent(tenantCreatedEvent);
    }

    public void publishTenantUpdatedEvent(Tenant tenant) {
        TenantUpdatedEvent tenantUpdatedEvent =
            new TenantUpdatedEvent(this, tenant);

        applicationEventPublisher.publishEvent(tenantUpdatedEvent);
    }

    public void publishTenantDeletedEvent(Tenant tenant) {
        TenantDeletedEvent tenantDeletedEvent =
            new TenantDeletedEvent(this, tenant);

        applicationEventPublisher.publishEvent(tenantDeletedEvent);
    }
}