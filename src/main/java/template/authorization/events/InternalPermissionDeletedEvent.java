package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.InternalPermission;

public class InternalPermissionDeletedEvent extends ApplicationEvent {
    private final InternalPermission permission;

    public InternalPermissionDeletedEvent(Object source,
                                          InternalPermission permission) {
        super(source);
        this.permission = permission;
    }

    public InternalPermission getPermission() {
        return permission;
    }
}