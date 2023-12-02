package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.TenantUserPassword;

public class TenantUserPasswordUpdatedEvent extends ApplicationEvent {
    private final TenantUserPassword userPassword;

    public TenantUserPasswordUpdatedEvent(Object source,
                                          TenantUserPassword userPassword) {
        super(source);
        this.userPassword = userPassword;
    }

    public TenantUserPassword getUser() {
        return userPassword;
    }
}