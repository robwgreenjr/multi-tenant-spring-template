package template.authentication.repositories;

import template.authentication.entities.TenantUserPasswordEntity;

import java.util.Optional;

public interface TenantUserPasswordRepository {
    Optional<TenantUserPasswordEntity> getById(Integer id);

    Optional<TenantUserPasswordEntity> getByUserEmail(String email);

    void save(TenantUserPasswordEntity userPassword);

    void delete(TenantUserPasswordEntity userPassword);
}
