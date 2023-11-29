package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.InternalUserPassword;

public class InternalUserPasswordCreatedEvent extends ApplicationEvent {
    private final InternalUserPassword userPasswordModel;

    public InternalUserPasswordCreatedEvent(Object source,
                                            InternalUserPassword userPasswordModel) {
        super(source);
        this.userPasswordModel = userPasswordModel;
    }

    public InternalUserPassword getUserPasswordModel() {
        return userPasswordModel;
    }
}