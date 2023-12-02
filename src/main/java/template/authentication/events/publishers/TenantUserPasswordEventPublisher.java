package template.authentication.events.publishers;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import template.authentication.events.TenantUserPasswordCreatedEvent;
import template.authentication.events.TenantUserPasswordDeletedEvent;
import template.authentication.events.TenantUserPasswordUpdatedEvent;
import template.authentication.models.TenantUserPassword;

@Component
public class TenantUserPasswordEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    public TenantUserPasswordEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishUserPasswordCreatedEvent(
        TenantUserPassword userPassword) {
        TenantUserPasswordCreatedEvent userPasswordCreatedEvent =
            new TenantUserPasswordCreatedEvent(this, userPassword);

        applicationEventPublisher.publishEvent(userPasswordCreatedEvent);
    }

    public void publishUserPasswordUpdatedEvent(
        TenantUserPassword userPassword) {
        TenantUserPasswordUpdatedEvent userPasswordUpdatedEvent =
            new TenantUserPasswordUpdatedEvent(this, userPassword);

        applicationEventPublisher.publishEvent(userPasswordUpdatedEvent);
    }

    public void publishUserPasswordDeletedEvent(
        TenantUserPassword userPassword) {
        TenantUserPasswordDeletedEvent userPasswordDeletedEvent =
            new TenantUserPasswordDeletedEvent(this, userPassword);

        applicationEventPublisher.publishEvent(userPasswordDeletedEvent);
    }
}