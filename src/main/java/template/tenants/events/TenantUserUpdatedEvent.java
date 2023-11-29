package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.TenantUser;

public class TenantUserUpdatedEvent extends ApplicationEvent {
    private final TenantUser user;

    public TenantUserUpdatedEvent(Object source, TenantUser user) {
        super(source);
        this.user = user;
    }

    public TenantUser getUser() {
        return user;
    }
}