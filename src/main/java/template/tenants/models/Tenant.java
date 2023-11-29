package template.tenants.models;

import org.apache.commons.validator.routines.EmailValidator;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class Tenant {
    private UUID id;
    private String companyName;
    private String email;
    private String subdomain;
    private String phone;
    private Instant createdOn;
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

    public void setSubdomainFromEmail() {
        if (this.getSubdomain() == null) {
            String[] findSubdomain = this.getEmail().split("@");
            this.setSubdomain(
                findSubdomain[findSubdomain.length - 1].split("\\.")[0]);
        }
    }

    public boolean checkIfValidEmail() {
        if (this.getEmail() == null) {
            return false;
        }

        return EmailValidator.getInstance().isValid(this.getEmail());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tenant that = (Tenant) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(companyName, that.companyName) &&
            Objects.equals(email, that.email) &&
            Objects.equals(subdomain, that.subdomain) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(updatedOn, that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, companyName, email, subdomain, phone, createdOn,
            updatedOn);
    }

    @Override
    public String toString() {
        return "TenantModel{" +
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
