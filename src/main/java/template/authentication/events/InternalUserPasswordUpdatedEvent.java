package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.InternalUserPassword;

public class InternalUserPasswordUpdatedEvent extends ApplicationEvent {
    private final InternalUserPassword userPasswordModel;

    public InternalUserPasswordUpdatedEvent(Object source,
                                            InternalUserPassword userPasswordModel) {
        super(source);
        this.userPasswordModel = userPasswordModel;
    }

    public InternalUserPassword getUser() {
        return userPasswordModel;
    }
}