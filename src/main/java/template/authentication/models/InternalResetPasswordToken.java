package template.authentication.models;


import template.internal.models.InternalUser;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class InternalResetPasswordToken {
    private InternalUser user;
    private UUID token;
    private LocalDateTime createdOn;
    private String password;
    private String passwordConfirmation;

    public InternalUser getUser() {
        return user;
    }

    public void setUser(InternalUser user) {
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
        if (!(o instanceof InternalResetPasswordToken that)) return false;
        return Objects.equals(getUser(), that.getUser()) &&
            Objects.equals(getToken(), that.getToken()) &&
            Objects.equals(getCreatedOn(), that.getCreatedOn()) &&
            Objects.equals(getPassword(), that.getPassword()) &&
            Objects.equals(getPasswordConfirmation(),
                that.getPasswordConfirmation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getToken(), getCreatedOn(),
            getPassword(),
            getPasswordConfirmation());
    }

    public String getCreatePasswordUri() {
        return "/create-password/" + this.getToken();
    }
}