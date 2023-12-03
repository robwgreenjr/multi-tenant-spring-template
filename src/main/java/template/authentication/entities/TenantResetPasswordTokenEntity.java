package template.authentication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import template.tenants.entities.TenantUserEntity;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "authentication_user_reset_password_token", schema = "tenant")
public class TenantResetPasswordTokenEntity {
    @ManyToOne(cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "user_id")
    private TenantUserEntity user;
    @Id
    private UUID token;
    @Generated
    private Instant createdOn;
}