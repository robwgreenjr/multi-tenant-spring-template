package template.authorization.repositories;

import org.springframework.stereotype.Service;
import template.authorization.entities.TenantPermissionEntity;
import template.database.models.Query;
import template.database.services.QueryBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class TenantPermissionRepositoryImpl
    implements TenantPermissionRepository {
    private final ITenantPermissionRepository permissionRepository;
    private final QueryBuilder<TenantPermissionEntity, Integer> queryBuilder;

    public TenantPermissionRepositoryImpl(
        ITenantPermissionRepository permissionRepository,
        QueryBuilder<TenantPermissionEntity, Integer> queryBuilder) {
        this.permissionRepository = permissionRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(TenantPermissionEntity.class, query);
    }

    @Override
    public void delete(TenantPermissionEntity permission) {
        permissionRepository.delete(permission);
    }

    @Override
    public Optional<TenantPermissionEntity> getById(Integer id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<TenantPermissionEntity> getByIdList(List<Integer> ids) {
        return permissionRepository.getListByIdIn(ids);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<TenantPermissionEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(TenantPermissionEntity.class, query);
    }

    @Override
    public TenantPermissionEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public TenantPermissionEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<TenantPermissionEntity> getSingle(Query<Integer> query) {
        TenantPermissionEntity entity =
            queryBuilder.getSingle(TenantPermissionEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(TenantPermissionEntity permission) {
        permissionRepository.save(permission);
    }

    @Override
    public void saveList(List<TenantPermissionEntity> entityList) {
        permissionRepository.saveAllAndFlush(entityList);
    }
}
