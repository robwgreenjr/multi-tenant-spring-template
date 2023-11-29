package template.tenants.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import template.tenants.events.TenantEmailConfirmationCreatedEvent;
import template.tenants.models.TenantEmailConfirmation;

@Service
public class TenantEmailConfirmationEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantEmailConfirmationEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishTenantEmailConfirmationCreatedEvent(
        TenantEmailConfirmation tenantEmailConfirmationModel) {
        TenantEmailConfirmationCreatedEvent tenantEmailConfirmationModelCreatedEvent =
            new TenantEmailConfirmationCreatedEvent(this, tenantEmailConfirmationModel);

        applicationEventPublisher.publishEvent(tenantEmailConfirmationModelCreatedEvent);
    }
}
