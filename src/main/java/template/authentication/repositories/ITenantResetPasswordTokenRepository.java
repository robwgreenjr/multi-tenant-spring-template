package template.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import template.authentication.entities.TenantResetPasswordTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface ITenantResetPasswordTokenRepository
    extends JpaRepository<TenantResetPasswordTokenEntity, Integer> {
    Optional<TenantResetPasswordTokenEntity> getByUserEmail(String email);

    Optional<TenantResetPasswordTokenEntity> getByToken(UUID token);
}