package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.TenantPermission;

public class TenantPermissionCreatedEvent extends ApplicationEvent {
    private final TenantPermission permission;

    public TenantPermissionCreatedEvent(Object source,
                                        TenantPermission permission) {
        super(source);
        this.permission = permission;
    }

    public TenantPermission getPermission() {
        return permission;
    }
}