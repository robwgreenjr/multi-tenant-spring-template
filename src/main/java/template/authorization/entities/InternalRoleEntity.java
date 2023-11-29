package template.authorization.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import template.internal.entities.InternalUserEntity;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "authorization_role", schema = "internal")
public class InternalRoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorization_role_permission", schema = "internal",
        joinColumns = {@JoinColumn(name = "role_id")},
        inverseJoinColumns = {@JoinColumn(name = "permission_id")})
    private Set<InternalPermissionEntity> permissions = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "authorization_role_user", schema = "internal",
        joinColumns = {@JoinColumn(name = "role_id")},
        inverseJoinColumns = {@JoinColumn(name = "user_id")})
    private Set<InternalUserEntity> users = new HashSet<>();

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

    public Set<InternalPermissionEntity> getPermissions() {
        return permissions;
    }

    public void setPermissions(
        Set<InternalPermissionEntity> permissions) {
        this.permissions = permissions;
    }

    public Set<InternalUserEntity> getUsers() {
        return users;
    }

    public void setUsers(
        Set<InternalUserEntity> users) {
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
    public String toString() {
        return "InternalRoleEntity{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", permissions=" + permissions +
            ", users=" + users +
            ", createdOn=" + createdOn +
            ", updatedOn=" + updatedOn +
            '}';
    }
}