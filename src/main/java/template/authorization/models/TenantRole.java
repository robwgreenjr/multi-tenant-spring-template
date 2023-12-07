package template.authorization.models;

import template.tenants.models.TenantUser;

import java.util.Objects;
import java.util.Set;

public class TenantRole {
    private Integer id;
    private String name;
    private String description;
    private Set<TenantPermission> permissions;
    private Set<TenantUser> users;

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

    public Set<TenantPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(
        Set<TenantPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<TenantUser> getUsers() {
        return users;
    }

    public void setUsers(Set<TenantUser> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantRole that = (TenantRole) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(permissions, that.permissions) &&
            Objects.equals(users, that.users);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, permissions, users);
    }

    @Override
    public String toString() {
        return "TenantRole{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", permissions=" + permissions +
            ", users=" + users +
            '}';
    }
}