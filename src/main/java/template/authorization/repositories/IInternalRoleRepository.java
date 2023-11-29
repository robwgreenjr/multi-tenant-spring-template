package template.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import template.authorization.entities.InternalRoleEntity;

import java.util.List;
import java.util.Optional;

public interface IInternalRoleRepository extends JpaRepository<InternalRoleEntity, Integer> {

    Optional<InternalRoleEntity> getByName(String name);

    List<InternalRoleEntity> getListByUsersId(Integer id);

    List<InternalRoleEntity> getListByIdIn(List<Integer> ids);
}