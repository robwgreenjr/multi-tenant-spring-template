package template.tenants.services;

import org.springframework.stereotype.Service;
import template.tenants.exceptions.TenantEmailConfirmationNotFoundException;
import template.tenants.mappers.TenantActivationConfirmationMapper;
import template.tenants.models.Tenant;
import template.tenants.models.TenantActivationConfirmation;
import template.tenants.models.TenantEmailConfirmation;

import java.util.Optional;

@Service
public class TenantRegistrationImpl implements TenantRegistration {
    private final TenantManager tenantManager;
    private final TenantEmailConfirmationManager tenantEmailConfirmationManager;
    private final TenantUserManager tenantUserManager;
    private final TenantActivationConfirmationMapper
        tenantActivationConfirmationMapper;

    public TenantRegistrationImpl(TenantManager tenantManager,
                                  TenantEmailConfirmationManager tenantEmailConfirmationManager,
                                  TenantUserManager tenantUserManager,
                                  TenantActivationConfirmationMapper tenantActivationConfirmationMapper) {
        this.tenantManager = tenantManager;
        this.tenantEmailConfirmationManager = tenantEmailConfirmationManager;
        this.tenantUserManager = tenantUserManager;
        this.tenantActivationConfirmationMapper =
            tenantActivationConfirmationMapper;
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

        tenantUserManager.create(
            tenantActivationConfirmationMapper.toTenantUser(
                tenantActivationConfirmation));

//        tenantEmailConfirmationManager.delete(tenantEmailConfirmation.get());
    }
}