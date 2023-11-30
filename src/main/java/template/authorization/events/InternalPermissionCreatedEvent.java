package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.InternalPermission;

public class InternalPermissionCreatedEvent extends ApplicationEvent {
    private final InternalPermission permission;

    public InternalPermissionCreatedEvent(Object source,
                                          InternalPermission permission) {
        super(source);
        this.permission = permission;
    }

    public InternalPermission getPermission() {
        return permission;
    }
}