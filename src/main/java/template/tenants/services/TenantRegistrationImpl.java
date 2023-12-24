package template.tenants.services;

import org.springframework.stereotype.Service;
import template.tenants.exceptions.TenantEmailConfirmationNotFoundException;
import template.tenants.helpers.TenantActivation;
import template.tenants.mappers.TenantActivationConfirmationMapper;
import template.tenants.models.Tenant;
import template.tenants.models.TenantActivationConfirmation;
import template.tenants.models.TenantEmailConfirmation;
import template.tenants.models.TenantUser;

import java.util.Optional;

@Service
public class TenantRegistrationImpl implements TenantRegistration {
    private final TenantManager tenantManager;
    private final TenantEmailConfirmationManager tenantEmailConfirmationManager;
    private final TenantUserManager tenantUserManager;
    private final TenantActivationConfirmationMapper
        tenantActivationConfirmationMapper;
    private final TenantActivation tenantActivation;


    public TenantRegistrationImpl(TenantManager tenantManager,
                                  TenantEmailConfirmationManager tenantEmailConfirmationManager,
                                  TenantUserManager tenantUserManager,
                                  TenantActivationConfirmationMapper tenantActivationConfirmationMapper,
                                  TenantActivation tenantActivation) {
        this.tenantManager = tenantManager;
        this.tenantEmailConfirmationManager = tenantEmailConfirmationManager;
        this.tenantUserManager = tenantUserManager;
        this.tenantActivationConfirmationMapper =
            tenantActivationConfirmationMapper;
        this.tenantActivation = tenantActivation;
    }


    @Override
    public void register(Tenant tenant) {
        tenant = tenantManager.create(tenant);

        tenantEmailConfirmationManager.create(
            new TenantEmailConfirmation(tenant));
    }

    @Override
    public void activateTenant(
        TenantActivationConfirmation tenantActivationConfirmation) {
        Optional<TenantEmailConfirmation> tenantEmailConfirmation =
            tenantEmailConfirmationManager.getByToken(
                tenantActivationConfirmation.getToken());
        if (tenantEmailConfirmation.isEmpty()) {
            throw new TenantEmailConfirmationNotFoundException();
        }

        TenantUser newTenant = tenantActivationConfirmationMapper.toTenantUser(
            tenantActivationConfirmation);
        TenantUser tenantUser = tenantUserManager.create(newTenant);

        tenantActivation.setInitialAuthorization(tenantUser);

        tenantEmailConfirmationManager.delete(tenantEmailConfirmation.get());
    }
}
