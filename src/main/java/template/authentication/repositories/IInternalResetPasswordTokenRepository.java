package template.authentication.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import template.authentication.entities.InternalResetPasswordTokenEntity;

import java.util.Optional;
import java.util.UUID;

public interface IInternalResetPasswordTokenRepository
    extends JpaRepository<InternalResetPasswordTokenEntity, Integer> {
    Optional<InternalResetPasswordTokenEntity> getByUserEmail(String email);

    Optional<InternalResetPasswordTokenEntity> getByToken(UUID token);
}