package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.TenantRole;

public class TenantRoleDeletedEvent extends ApplicationEvent {
    private final TenantRole role;

    public TenantRoleDeletedEvent(Object source, TenantRole role) {
        super(source);
        this.role = role;
    }

    public TenantRole getRole() {
        return role;
    }
}