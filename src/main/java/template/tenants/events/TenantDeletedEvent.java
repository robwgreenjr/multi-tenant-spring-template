package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.Tenant;

public class TenantDeletedEvent extends ApplicationEvent {
    private final Tenant tenantModel;

    public TenantDeletedEvent(Object source, Tenant tenantModel) {
        super(source);
        this.tenantModel = tenantModel;
    }

    public Tenant getTenant() {
        return tenantModel;
    }
}