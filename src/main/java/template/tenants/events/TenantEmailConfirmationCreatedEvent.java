package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.TenantEmailConfirmation;

public class TenantEmailConfirmationCreatedEvent extends ApplicationEvent {
    private final TenantEmailConfirmation tenantEmailConfirmation;

    public TenantEmailConfirmationCreatedEvent(Object source,
                                               TenantEmailConfirmation tenantEmailConfirmation) {
        super(source);
        this.tenantEmailConfirmation = tenantEmailConfirmation;
    }

    public TenantEmailConfirmation getTenant() {
        return tenantEmailConfirmation;
    }
}