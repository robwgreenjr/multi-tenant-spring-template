package template.tenants.models;

import java.util.Objects;
import java.util.UUID;

public class TenantActivationConfirmation {
    private UUID token;
    private String email;
    private String firstName;
    private String lastName;
    private String phone;

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TenantActivationConfirmation that = (TenantActivationConfirmation) o;
        return Objects.equals(token, that.token) &&
            Objects.equals(email, that.email) &&
            Objects.equals(firstName, that.firstName) &&
            Objects.equals(lastName, that.lastName) &&
            Objects.equals(phone, that.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(token, email, firstName, lastName, phone);
    }

    @Override
    public String toString() {
        return "TenantActivationConfirmation{" +
            "token='" + token + '\'' +
            ", email='" + email + '\'' +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", phone='" + phone + '\'' +
            '}';
    }
}
