package template.authentication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.TenantId;
import template.tenants.entities.TenantUserEntity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authentication_user_reset_password_token", schema = "tenant")
public class TenantResetPasswordTokenEntity {
    @Id
    private UUID token;
    @TenantId
    private UUID tenantId;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private TenantUserEntity user;
    @Generated
    private Instant createdOn;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
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
        TenantResetPasswordTokenEntity that =
            (TenantResetPasswordTokenEntity) o;
        return Objects.equals(token, that.token) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(user, that.user) &&
            Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, tenantId, user, createdOn);
    }

    @Override
    public String toString() {
        return "TenantResetPasswordTokenEntity{" +
            "token=" + token +
            ", tenantId=" + tenantId +
            ", user=" + user +
            ", createdOn=" + createdOn +
            '}';
    }
}