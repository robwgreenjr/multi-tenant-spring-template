package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.TenantUserPassword;

public class TenantUserPasswordDeletedEvent extends ApplicationEvent {
    private final TenantUserPassword userPassword;

    public TenantUserPasswordDeletedEvent(Object source,
                                          TenantUserPassword userPassword) {
        super(source);
        this.userPassword = userPassword;
    }

    public TenantUserPassword getUser() {
        return userPassword;
    }
}