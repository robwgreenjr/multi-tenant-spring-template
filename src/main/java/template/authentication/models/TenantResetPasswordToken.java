package template.authentication.models;


import template.tenants.models.TenantUser;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class TenantResetPasswordToken {
    private TenantUser user;
    private UUID token;
    private LocalDateTime createdOn;
    private String password;
    private String passwordConfirmation;

    public TenantUser getUser() {
        return user;
    }

    public void setUser(TenantUser user) {
        this.user = user;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantResetPasswordToken that = (TenantResetPasswordToken) o;
        return Objects.equals(user, that.user) &&
            Objects.equals(token, that.token) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(password, that.password) &&
            Objects.equals(passwordConfirmation,
                that.passwordConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, token, createdOn, password,
            passwordConfirmation);
    }

    public String getCreatePasswordUri() {
        return "/create-password/" + this.getToken();
    }

    @Override
    public String toString() {
        return "TenantResetPasswordToken{" +
            "user=" + user +
            ", token=" + token +
            ", createdOn=" + createdOn +
            ", password='" + password + '\'' +
            ", passwordConfirmation='" + passwordConfirmation + '\'' +
            '}';
    }
}