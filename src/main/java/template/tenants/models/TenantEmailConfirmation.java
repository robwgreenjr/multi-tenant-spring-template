package template.tenants.models;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TenantEmailConfirmation {
    private UUID token;
    private Tenant tenant;
    private Instant createdOn;

    public TenantEmailConfirmation(Tenant tenant) {
        this.tenant = tenant;
    }

    public TenantEmailConfirmation() {
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Tenant getTenant() {
        return tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantEmailConfirmation that = (TenantEmailConfirmation) o;
        return Objects.equals(token, that.token) &&
            Objects.equals(tenant, that.tenant) &&
            Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tenant, createdOn);
    }

    @Override
    public String toString() {
        return "TenantEmailConfirmationModel{" +
            "token=" + token +
            ", tenant=" + tenant +
            ", createdOn=" + createdOn +
            '}';
    }
}
