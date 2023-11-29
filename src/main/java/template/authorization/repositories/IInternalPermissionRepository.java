package template.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.authorization.entities.InternalPermissionEntity;

import java.util.List;

public interface IInternalPermissionRepository
    extends JpaRepository<InternalPermissionEntity, Integer>,
    JpaSpecificationExecutor<InternalPermissionEntity> {
    List<InternalPermissionEntity> getListByIdIn(List<Integer> ids);
}