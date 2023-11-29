package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.Tenant;

public class TenantCreatedEvent extends ApplicationEvent {
    private final Tenant tenantModel;

    public TenantCreatedEvent(Object source, Tenant tenantModel) {
        super(source);
        this.tenantModel = tenantModel;
    }

    public Tenant getTenant() {
        return tenantModel;
    }
}