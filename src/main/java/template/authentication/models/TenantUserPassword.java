package template.authentication.models;

import template.authentication.exceptions.PasswordMismatchException;
import template.tenants.models.TenantUser;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TenantUserPassword {
    private Integer id;
    private TenantUser user;
    private String password;
    private String passwordConfirmation;
    private String previousPassword;
    private String currentPassword;
    private UUID token;
    private Instant createdOn;
    private String emailConfirmation;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public TenantUser getUser() {
        return user;
    }

    public void setUser(TenantUser user) {
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

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getEmailConfirmation() {
        return emailConfirmation;
    }

    public void setEmailConfirmation(String emailConfirmation) {
        this.emailConfirmation = emailConfirmation;
    }

    public void confirmMatchingPasswords() {
        if (!this.password.equals(this.passwordConfirmation)) {
            throw new PasswordMismatchException();
        }
    }

    public void validatePassword() {
        confirmMatchingPasswords();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantUserPassword that = (TenantUserPassword) o;
        return Objects.equals(id, that.id) &&
            Objects.equals(user, that.user) &&
            Objects.equals(password, that.password) &&
            Objects.equals(passwordConfirmation,
                that.passwordConfirmation) &&
            Objects.equals(previousPassword, that.previousPassword) &&
            Objects.equals(currentPassword, that.currentPassword) &&
            Objects.equals(token, that.token) &&
            Objects.equals(createdOn, that.createdOn) &&
            Objects.equals(emailConfirmation, that.emailConfirmation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, user, password, passwordConfirmation,
            previousPassword, currentPassword, token, createdOn,
            emailConfirmation);
    }

    @Override
    public String toString() {
        return "TenantUserPassword{" +
            "id=" + id +
            ", user=" + user +
            ", password='" + password + '\'' +
            ", passwordConfirmation='" + passwordConfirmation + '\'' +
            ", previousPassword='" + previousPassword + '\'' +
            ", currentPassword='" + currentPassword + '\'' +
            ", token=" + token +
            ", createdOn=" + createdOn +
            ", emailConfirmation='" + emailConfirmation + '\'' +
            '}';
    }
}