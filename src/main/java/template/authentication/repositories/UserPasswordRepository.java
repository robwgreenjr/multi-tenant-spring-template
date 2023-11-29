package template.authentication.repositories;

import template.authentication.entities.InternalUserPasswordEntity;

import java.util.Optional;

public interface UserPasswordRepository {
    Optional<InternalUserPasswordEntity> getById(Integer id);

    Optional<InternalUserPasswordEntity> getByUserEmail(String email);

    void save(InternalUserPasswordEntity userPassword);

    void delete(InternalUserPasswordEntity userPassword);
}
