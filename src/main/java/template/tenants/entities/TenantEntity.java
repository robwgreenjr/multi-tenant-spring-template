package template.tenants.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tenant", schema = "internal")
public class TenantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String companyName;
    private String email;
    private String subdomain;
    private String phone;
    @Generated()
    private Instant createdOn;
    @Generated()
    private Instant updatedOn;

    public String getSubdomain() {
        return subdomain;
    }

    public void setSubdomain(String subdomain) {
        this.subdomain = subdomain;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Instant getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Instant createdOn) {
        this.createdOn = createdOn;
    }

    public Instant getUpdatedOn() {
        return updatedOn;
    }

    public void setUpdatedOn(Instant updatedOn) {
        this.updatedOn = updatedOn;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantEntity tenant = (TenantEntity) o;
        return Objects.equals(id, tenant.id) &&
            Objects.equals(companyName, tenant.companyName) &&
            Objects.equals(email, tenant.email) &&
            Objects.equals(subdomain, tenant.subdomain) &&
            Objects.equals(phone, tenant.phone) &&
            Objects.equals(createdOn, tenant.createdOn) &&
            Objects.equals(updatedOn, tenant.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, email, subdomain, phone, createdOn,
            updatedOn);
    }

    @Override
    public String toString() {
        return "Tenant{" +
            "id=" + id +
            ", companyName='" + companyName + '\'' +
            ", email='" + email + '\'' +
            ", subdomain='" + subdomain + '\'' +
            ", phone='" + phone + '\'' +
            ", createdOn=" + createdOn +
            ", updatedOn=" + updatedOn +
            '}';
    }
}
