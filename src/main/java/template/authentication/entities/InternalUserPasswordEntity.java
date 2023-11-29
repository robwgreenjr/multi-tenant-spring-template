package template.authentication.entities;

import jakarta.persistence.*;
import org.hibernate.annotations.Generated;
import template.internal.entities.InternalUserEntity;

import java.time.Instant;

@Entity
@Table(name = "authentication_user_password", schema = "internal")
public class InternalUserPasswordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    private InternalUserEntity user;
    private String password;
    private String previousPassword;
    @Generated
    private Instant createdOn;
    @Generated
    private Instant updatedOn;

}