package template.authorization.services;

import org.springframework.stereotype.Service;
import template.authorization.entities.InternalPermissionEntity;
import template.authorization.exceptions.PermissionNotFoundException;
import template.authorization.mappers.InternalPermissionMapper;
import template.authorization.models.InternalPermission;
import template.authorization.repositories.InternalPermissionRepository;
import template.database.models.Query;
import template.database.models.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InternalPermissionManagerImpl
    implements InternalPermissionManager {
    private final InternalPermissionRepository permissionRepository;
    private final InternalPermissionMapper permissionMapper;

    public InternalPermissionManagerImpl(
        InternalPermissionRepository permissionRepository,
        InternalPermissionMapper permissionMapper) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
    }

    @Override
    public InternalPermission create(InternalPermission permission) {
        InternalPermissionEntity newEntity =
            permissionMapper.toEntity(permission);
        permissionRepository.save(newEntity);

        return permissionMapper.entityToObject(newEntity);
    }

    @Override
    public List<InternalPermission> createAll(
        List<InternalPermission> permissionList) {
        List<InternalPermissionEntity> entityList =
            permissionMapper.toEntityList(permissionList);

        permissionRepository.saveList(entityList);

        return permissionMapper.entityToList(entityList);
    }

    @Override
    public void delete(Integer id) {
        Optional<InternalPermissionEntity> findEntity =
            permissionRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        permissionRepository.delete(findEntity.get());
    }

    @Override
    public InternalPermission getById(Integer id) {
        Optional<InternalPermissionEntity> entity =
            permissionRepository.getById(id);

        if (entity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        return permissionMapper.entityToObject(entity.get());
    }

    @Override
    public List<InternalPermission> getByIdList(List<Integer> idList) {
        List<InternalPermissionEntity> permissionList =
            permissionRepository.getByIdList(idList);

        return permissionMapper.entityToList(permissionList);
    }

    @Override
    public QueryResult<InternalPermission> getList(Query<Integer> query) {
        List<InternalPermissionEntity> entityList =
            permissionRepository.getList(query);

        QueryResult<InternalPermission> result = new QueryResult<>();
        result.setData(permissionMapper.entityToList(entityList));
        result.getMeta().setPageCount(entityList.size());

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        result.getMeta().setCount(permissionRepository.count(query));

        return result;
    }

    @Override
    public QueryResult<InternalPermission> getSingle(Query<Integer> query) {
        Optional<InternalPermissionEntity> entity =
            permissionRepository.getSingle(query);

        if (entity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        QueryResult<InternalPermission> result = new QueryResult<>();
        List<InternalPermission> data = new ArrayList<>();
        data.add(permissionMapper.entityToObject(entity.get()));
        result.setData(data);

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        return result;
    }

    @Override
    public InternalPermission update(Integer id,
                                     InternalPermission permission) {
        InternalPermissionEntity entity = permissionMapper.toEntity(permission);
        entity.setId(id);

        permissionRepository.save(entity);

        return permission;
    }

    @Override
    public List<InternalPermission> updateAll(
        List<InternalPermission> permissionList) {
        List<InternalPermissionEntity> entityList =
            permissionMapper.toEntityList(permissionList);

        permissionRepository.saveList(entityList);

        return permissionMapper.entityToList(entityList);
    }

    @Override
    public InternalPermission updatePartial(Integer id,
                                            InternalPermission permission) {
        Optional<InternalPermissionEntity> findEntity =
            permissionRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        InternalPermissionEntity foundEntity = findEntity.get();
        permission.setId(foundEntity.getId());

        permissionMapper.update(foundEntity, permission);
        permissionRepository.save(foundEntity);

        return permissionMapper.entityToObject(foundEntity);
    }
}