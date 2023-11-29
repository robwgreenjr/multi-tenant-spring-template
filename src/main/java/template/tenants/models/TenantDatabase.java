package template.tenants.models;

import template.tenants.entities.TenantEntity;

import java.time.Instant;
import java.util.Objects;

public class TenantDatabase {
    private Integer id;
    private TenantEntity tenant;
    private String url;
    private String username;
    private String password;
    private Integer minimumIdle;
    private Integer maximumPoolSize;
    private Instant createdOn;
    private Instant updatedOn;

    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public Integer getMinimumIdle() {
        return minimumIdle;
    }

    public void setMinimumIdle(Integer minimumIdle) {
        this.minimumIdle = minimumIdle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TenantEntity getTenant() {
        return tenant;
    }

    public void setTenant(TenantEntity tenant) {
        this.tenant = tenant;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
        TenantDatabase that = (TenantDatabase) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(tenant, that.tenant) &&
            Objects.equals(url, that.url) &&
            Objects.equals(username, that.username) &&
            Objects.equals(password, that.password) &&
            Objects.equals(minimumIdle, that.minimumIdle) &&
            Objects.equals(maximumPoolSize, that.maximumPoolSize) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(updatedOn, that.updatedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tenant, url, username, password, minimumIdle,
            maximumPoolSize, createdOn, updatedOn);
    }

    @Override
    public String toString() {
        return "TenantDatabase{" +
            "id=" + id +
            ", tenant=" + tenant +
            ", url='" + url + '\'' +
            ", username='" + username + '\'' +
            ", password='" + password + '\'' +
            ", minimumIdle=" + minimumIdle +
            ", maximumPoolSize=" + maximumPoolSize +
            ", createdOn=" + createdOn +
            ", updatedOn=" + updatedOn +
            '}';
    }
}
