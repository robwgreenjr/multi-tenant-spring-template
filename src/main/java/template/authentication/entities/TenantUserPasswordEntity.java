package template.authentication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.TenantId;
import template.tenants.entities.TenantUserEntity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authentication_user_password", schema = "tenant")
public class TenantUserPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @TenantId
    private UUID tenantId;
    @ManyToOne
    private TenantUserEntity user;
    private String password;
    private String previousPassword;
    @Generated
    private Instant createdOn;
    @Generated
    private Instant updatedOn;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UUID getTenantId() {
        return tenantId;
    }

    public void setTenantId(UUID tenantId) {
        this.tenantId = tenantId;
    }

    public TenantUserEntity getUser() {
        return user;
    }

    public void setUser(TenantUserEntity user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreviousPassword() {
        return previousPassword;
    }

    public void setPreviousPassword(String previousPassword) {
        this.previousPassword = previousPassword;
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
        TenantUserPasswordEntity that = (TenantUserPasswordEntity) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(user, that.user) &&
            Objects.equals(password, that.password) &&
            Objects.equals(previousPassword, that.previousPassword) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(updatedOn, that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, user, password, previousPassword,
            createdOn, updatedOn);
    }

    @Override
    public String toString() {
        return "TenantUserPasswordEntity{" +
            "id=" + id +
            ", tenantId=" + tenantId +
            ", user=" + user +
            ", password='" + password + '\'' +
            ", previousPassword='" + previousPassword + '\'' +
            ", createdOn=" + createdOn +
            ", updatedOn=" + updatedOn +
            '}';
    }
}