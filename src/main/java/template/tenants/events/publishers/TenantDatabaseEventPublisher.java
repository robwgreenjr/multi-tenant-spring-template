package template.tenants.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.tenants.events.TenantDatabaseCreatedEvent;
import template.tenants.models.TenantDatabase;

@Service
public class TenantDatabaseEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantDatabaseEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTenantCreatedEvent(
        TenantDatabase TenantDatabase) {
        TenantDatabaseCreatedEvent tenantDatabaseCreatedEvent =
            new TenantDatabaseCreatedEvent(this, TenantDatabase);

        applicationEventPublisher.publishEvent(tenantDatabaseCreatedEvent);
    }
}
