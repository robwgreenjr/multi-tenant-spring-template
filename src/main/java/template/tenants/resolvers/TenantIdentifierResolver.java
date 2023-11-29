package template.tenants.resolvers;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

@Component
public class TenantIdentifierResolver
    implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {
    private UUID currentTenant = UUID.randomUUID();
    private boolean hasTenantBeenSet = false;

    public void setCurrentTenant(UUID tenant) {
        currentTenant = tenant;
        hasTenantBeenSet = true;
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        return Objects.requireNonNullElseGet(currentTenant, UUID::randomUUID)
            .toString();
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return false;
    }

    @Override
    public boolean isRoot(String tenantId) {
        return CurrentTenantIdentifierResolver.super.isRoot(tenantId);
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(
            AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

    public boolean isTenantSet() {
        return hasTenantBeenSet;
    }

    public void resetTenant() {
        currentTenant = null;
        hasTenantBeenSet = false;
    }
}
