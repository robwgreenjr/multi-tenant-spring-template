package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.Tenant;

public class TenantDeletedEvent extends ApplicationEvent {
    private final Tenant tenant;

    public TenantDeletedEvent(Object source, Tenant tenant) {
        super(source);
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return tenant;
    }
}