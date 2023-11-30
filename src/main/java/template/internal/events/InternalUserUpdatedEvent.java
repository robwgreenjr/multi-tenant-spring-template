package template.internal.events;

import org.springframework.context.ApplicationEvent;
import template.internal.models.InternalUser;

public class InternalUserUpdatedEvent extends ApplicationEvent {
    private final InternalUser user;

    public InternalUserUpdatedEvent(Object source, InternalUser user) {
        super(source);
        this.user = user;
    }

    public InternalUser getUser() {
        return user;
    }
}