package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.InternalUserPassword;

public class InternalUserPasswordDeletedEvent extends ApplicationEvent {
    private final InternalUserPassword userPasswordModel;

    public InternalUserPasswordDeletedEvent(Object source,
                                            InternalUserPassword userPasswordModel) {
        super(source);
        this.userPasswordModel = userPasswordModel;
    }

    public InternalUserPassword getUser() {
        return userPasswordModel;
    }
}