package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.TenantEmailConfirmation;

public class TenantEmailConfirmationCreatedEvent extends ApplicationEvent {
    private final TenantEmailConfirmation tenantEmailConfirmationModel;

    public TenantEmailConfirmationCreatedEvent(Object source,
                                               TenantEmailConfirmation tenantEmailConfirmationModel) {
        super(source);
        this.tenantEmailConfirmationModel = tenantEmailConfirmationModel;
    }

    public TenantEmailConfirmation getTenant() {
        return tenantEmailConfirmationModel;
    }
}