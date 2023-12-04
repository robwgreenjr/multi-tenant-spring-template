package template.authorization.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import org.hibernate.annotations.TenantId;
import template.tenants.entities.TenantUserEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "authorization_role", schema = "tenant")
public class TenantRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @TenantId
    private UUID tenantId;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorization_role_permission", schema = "internal",
        joinColumns = {@JoinColumn(name = "role_id")},
        inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<TenantPermissionEntity> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorization_role_user", schema = "internal",
        joinColumns = {@JoinColumn(name = "role_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<TenantUserEntity> users = new HashSet<>();

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<TenantPermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(
        Set<TenantPermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public Set<TenantUserEntity> getUsers() {
        return users;
    }

    public void setUsers(
        Set<TenantUserEntity> users) {
        this.users = users;
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
        TenantRoleEntity that = (TenantRoleEntity) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(tenantId, that.tenantId) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(permissions, that.permissions) &&
            Objects.equals(users, that.users) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(updatedOn, that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenantId, name, description, permissions, users,
            createdOn, updatedOn);
    }

    @Override
    public String toString() {
        return "TenantRoleEntity{" +
            "id=" + id +
            ", tenantId=" + tenantId +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", permissions=" + permissions +
            ", users=" + users +
            ", createdOn=" + createdOn +
            ", updatedOn=" + updatedOn +
            '}';
    }
}