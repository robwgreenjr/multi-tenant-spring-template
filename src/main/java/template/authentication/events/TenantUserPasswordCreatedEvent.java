package template.authentication.events;

import org.springframework.context.ApplicationEvent;
import template.authentication.models.TenantUserPassword;

public class TenantUserPasswordCreatedEvent extends ApplicationEvent {
    private final TenantUserPassword userPassword;

    public TenantUserPasswordCreatedEvent(Object source,
                                          TenantUserPassword userPassword) {
        super(source);
        this.userPassword = userPassword;
    }

    public TenantUserPassword getUserPasswordModel() {
        return userPassword;
    }
}