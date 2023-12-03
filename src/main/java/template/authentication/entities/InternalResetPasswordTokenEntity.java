package template.authentication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import template.internal.entities.InternalUserEntity;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "authentication_user_reset_password_token", schema = "internal")
public class InternalResetPasswordTokenEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private InternalUserEntity user;
    @Id
    private UUID token;
    @Generated
    private Instant createdOn;

    public InternalUserEntity getUser() {
        return user;
    }

    public void setUser(InternalUserEntity user) {
        this.user = user;
    }

    public UUID getToken() {
        return token;
    }

    public void setToken(UUID token) {
        this.token = token;
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
        InternalResetPasswordTokenEntity that =
            (InternalResetPasswordTokenEntity) o;
        return Objects.equals(user, that.user) &&
            Objects.equals(token, that.token) &&
            Objects.equals(createdOn, that.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user, token, createdOn);
    }

    @Override
    public String toString() {
        return "InternalResetPasswordTokenEntity{" +
            "user=" + user +
            ", token=" + token +
            ", createdOn=" + createdOn +
            '}';
    }
}