package template.tenants.services;

import org.springframework.stereotype.Service;
import template.global.services.Manager;
import template.global.services.QueryManager;
import template.tenants.models.Tenant;

import java.util.Optional;
import java.util.UUID;

@Service
public interface TenantManager extends QueryManager<Tenant, UUID>,
    Manager<Tenant, UUID> {
    Optional<Tenant> getById(UUID id);

    Optional<Tenant> getByEmail(String email);

    Optional<Tenant> getBySubdomain(String subdomain);
}