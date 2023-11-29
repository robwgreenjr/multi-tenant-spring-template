package template.authentication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import template.tenants.entities.TenantUserEntity;

import java.time.Instant;

@Entity
@Table(name = "authentication_user_password", schema = "tenant")
public class TenantUserPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private TenantUserEntity user;
    private String password;
    private String previousPassword;
    @Generated
    private Instant createdOn;
    @Generated
    private Instant updatedOn;

}