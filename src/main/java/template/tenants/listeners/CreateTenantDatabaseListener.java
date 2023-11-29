package template.tenants.listeners;

import template.tenants.events.TenantDatabaseCreatedEvent;
import template.tenants.repositories.TenantEmailConfirmationRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class CreateTenantDatabaseListener
    implements ApplicationListener<TenantDatabaseCreatedEvent> {

    private final TenantEmailConfirmationRepository
        tenantEmailConfirmationRepository;

    public CreateTenantDatabaseListener(
        TenantEmailConfirmationRepository tenantEmailConfirmationRepository) {
        this.tenantEmailConfirmationRepository =
            tenantEmailConfirmationRepository;
    }

    @Override
    public void onApplicationEvent(TenantDatabaseCreatedEvent event) {
        tenantEmailConfirmationRepository.deleteByTenant(
            event.getTenantDatabase().getTenant());
    }
}
