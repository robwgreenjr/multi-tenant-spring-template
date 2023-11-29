package template.tenants.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tenant_email_confirmation", schema = "internal")
public class TenantEmailConfirmationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID token;
    @ManyToOne
    @JoinColumn(name = "tenant_id")
    private TenantEntity tenant;
    @Generated()
    private Instant createdOn;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public void setTenant(TenantEntity tenant) {
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
        TenantEmailConfirmationEntity that = (TenantEmailConfirmationEntity) o;
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
        return "TenantEmailConfirmation{" +
            "token=" + token +
            ", tenant=" + tenant +
            ", createdOn=" + createdOn +
            '}';
    }
}
