package template.authorization.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import template.authorization.entities.TenantPermissionEntity;

import java.util.List;

public interface ITenantPermissionRepository
    extends JpaRepository<TenantPermissionEntity, Integer>,
    JpaSpecificationExecutor<TenantPermissionEntity> {
    List<TenantPermissionEntity> getListByIdIn(List<Integer> ids);
}