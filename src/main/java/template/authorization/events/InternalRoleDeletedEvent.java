package template.authorization.events;

import org.springframework.context.ApplicationEvent;
import template.authorization.models.InternalRole;

public class InternalRoleDeletedEvent extends ApplicationEvent {
    private final InternalRole role;

    public InternalRoleDeletedEvent(Object source, InternalRole role) {
        super(source);
        this.role = role;
    }

    public InternalRole getRole() {
        return role;
    }
}