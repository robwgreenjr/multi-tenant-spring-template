package template.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import template.authorization.entities.TenantRoleEntity;

import java.util.List;
import java.util.Optional;

public interface ITenantRoleRepository
    extends JpaRepository<TenantRoleEntity, Integer> {

    Optional<TenantRoleEntity> getByName(String name);

    List<TenantRoleEntity> getListByUsersId(Integer id);

    List<TenantRoleEntity> getListByIdIn(List<Integer> ids);
}