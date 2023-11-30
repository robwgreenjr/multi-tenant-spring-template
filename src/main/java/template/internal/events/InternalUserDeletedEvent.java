package template.internal.events;

import org.springframework.context.ApplicationEvent;
import template.internal.models.InternalUser;

public class InternalUserDeletedEvent extends ApplicationEvent {
    private final InternalUser user;

    public InternalUserDeletedEvent(Object source, InternalUser user) {
        super(source);
        this.user = user;
    }

    public InternalUser getUser() {
        return user;
    }
}