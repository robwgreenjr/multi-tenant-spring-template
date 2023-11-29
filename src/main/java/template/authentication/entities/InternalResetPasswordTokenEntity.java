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
        if (!(o instanceof InternalResetPasswordTokenEntity that)) return false;
        return Objects.equals(getUser(), that.getUser()) &&
            Objects.equals(getToken(), that.getToken()) &&
            Objects.equals(getCreatedOn(), that.getCreatedOn());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUser(), getToken(), getCreatedOn());
    }
}