package template.tenants.events;

import org.springframework.context.ApplicationEvent;
import template.tenants.models.TenantUser;

public class TenantUserCreatedEvent extends ApplicationEvent {
    private final TenantUser user;

    public TenantUserCreatedEvent(Object source, TenantUser user) {
        super(source);
        this.user = user;
    }

    public TenantUser getUser() {
        return user;
    }
}