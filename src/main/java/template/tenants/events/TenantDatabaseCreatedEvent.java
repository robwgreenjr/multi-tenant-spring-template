package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.TenantDatabase;

public class TenantDatabaseCreatedEvent extends ApplicationEvent {
    private final TenantDatabase TenantDatabase;

    public TenantDatabaseCreatedEvent(Object source,
                                      TenantDatabase TenantDatabase) {
        super(source);
        this.TenantDatabase = TenantDatabase;
    }

    public TenantDatabase getTenantDatabase() {
        return TenantDatabase;
    }
}