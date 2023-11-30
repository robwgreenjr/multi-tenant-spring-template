package template.internal.events;

import org.springframework.context.ApplicationEvent;
import template.internal.models.InternalUser;

public class InternalUserCreatedEvent extends ApplicationEvent {
    private final InternalUser user;

    public InternalUserCreatedEvent(Object source, InternalUser user) {
        super(source);
        this.user = user;
    }

    public InternalUser getUser() {
        return user;
    }
}