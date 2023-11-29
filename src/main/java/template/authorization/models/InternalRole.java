package template.authorization.models;

import template.internal.models.InternalUser;

import java.util.Objects;
import java.util.Set;

public class InternalRole {
    private Integer id;
    private String name;
    private String description;
    private Set<InternalPermission> permissions;
    private Set<InternalUser> users;

    public Set<InternalPermission> getPermissions() {
        return permissions;
    }

    public void setPermissions(
        Set<InternalPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<InternalUser> getUsers() {
        return users;
    }

    public void setUsers(Set<InternalUser> users) {
        this.users = users;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InternalRole that = (InternalRole) o;
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
        return "InternalRole{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", permissions=" + permissions +
            ", users=" + users +
            '}';
    }
}