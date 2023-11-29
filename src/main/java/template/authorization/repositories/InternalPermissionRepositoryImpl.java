package template.authorization.repositories;

import org.springframework.stereotype.Service;
import template.authorization.entities.InternalPermissionEntity;
import template.database.models.Query;
import template.database.services.QueryBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class InternalPermissionRepositoryImpl
    implements InternalPermissionRepository {
    private final IInternalPermissionRepository permissionRepository;
    private final QueryBuilder<InternalPermissionEntity, Integer> queryBuilder;

    public InternalPermissionRepositoryImpl(
        IInternalPermissionRepository permissionRepository,
        QueryBuilder<InternalPermissionEntity, Integer> queryBuilder) {
        this.permissionRepository = permissionRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(InternalPermissionEntity.class, query);
    }

    @Override
    public void delete(InternalPermissionEntity permission) {
        permissionRepository.delete(permission);
    }

    @Override
    public Optional<InternalPermissionEntity> getById(Integer id) {
        return permissionRepository.findById(id);
    }

    @Override
    public List<InternalPermissionEntity> getByIdList(List<Integer> ids) {
        return permissionRepository.getListByIdIn(ids);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<InternalPermissionEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(InternalPermissionEntity.class, query);
    }

    @Override
    public InternalPermissionEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public InternalPermissionEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<InternalPermissionEntity> getSingle(Query<Integer> query) {
        InternalPermissionEntity entity =
            queryBuilder.getSingle(InternalPermissionEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(InternalPermissionEntity permission) {
        permissionRepository.save(permission);
    }

    @Override
    public void saveList(List<InternalPermissionEntity> entityList) {
        permissionRepository.saveAllAndFlush(entityList);
    }
}
