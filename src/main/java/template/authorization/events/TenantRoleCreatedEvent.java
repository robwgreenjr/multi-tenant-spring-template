package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.TenantRole;

public class TenantRoleCreatedEvent extends ApplicationEvent {
    private final TenantRole role;

    public TenantRoleCreatedEvent(Object source, TenantRole role) {
        super(source);
        this.role = role;
    }

    public TenantRole getRole() {
        return role;
    }
}