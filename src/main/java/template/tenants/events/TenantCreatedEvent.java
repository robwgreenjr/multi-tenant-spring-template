package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.Tenant;

public class TenantCreatedEvent extends ApplicationEvent {
    private final Tenant tenant;

    public TenantCreatedEvent(Object source, Tenant tenant) {
        super(source);
        this.tenant = tenant;
    }

    public Tenant getTenant() {
        return tenant;
    }
}