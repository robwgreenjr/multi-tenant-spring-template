package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.TenantPermission;

public class TenantPermissionUpdatedEvent extends ApplicationEvent {
    private final TenantPermission permission;

    public TenantPermissionUpdatedEvent(Object source,
                                        TenantPermission permission) {
        super(source);
        this.permission = permission;
    }

    public TenantPermission getPermission() {
        return permission;
    }
}