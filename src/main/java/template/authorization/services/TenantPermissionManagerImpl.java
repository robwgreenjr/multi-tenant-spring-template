package template.authorization.services;

import org.springframework.stereotype.Service;
import template.authorization.entities.TenantPermissionEntity;
import template.authorization.events.publishers.TenantPermissionEventPublisher;
import template.authorization.exceptions.PermissionNotFoundException;
import template.authorization.mappers.TenantPermissionMapper;
import template.authorization.models.TenantPermission;
import template.authorization.repositories.TenantPermissionRepository;
import template.database.models.Query;
import template.database.models.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TenantPermissionManagerImpl
    implements TenantPermissionManager {
    private final TenantPermissionRepository permissionRepository;
    private final TenantPermissionMapper permissionMapper;
    private final TenantPermissionEventPublisher tenantPermissionEventPublisher;

    public TenantPermissionManagerImpl(
        TenantPermissionRepository permissionRepository,
        TenantPermissionMapper permissionMapper,
        TenantPermissionEventPublisher tenantPermissionEventPublisher) {
        this.permissionRepository = permissionRepository;
        this.permissionMapper = permissionMapper;
        this.tenantPermissionEventPublisher = tenantPermissionEventPublisher;
    }

    @Override
    public TenantPermission create(TenantPermission permission) {
        TenantPermissionEntity newEntity =
            permissionMapper.toEntity(permission);
        permissionRepository.save(newEntity);

        TenantPermission newPermission =
            permissionMapper.entityToObject(newEntity);
        tenantPermissionEventPublisher.publishTenantPermissionCreatedEvent(
            newPermission);

        return newPermission;
    }

    @Override
    public List<TenantPermission> createAll(
        List<TenantPermission> permissionList) {
        List<TenantPermissionEntity> newEntityList =
            permissionMapper.toEntityList(permissionList);

        permissionRepository.saveList(newEntityList);

        List<TenantPermission> newPermissions =
            permissionMapper.entityToList(newEntityList);
        for (TenantPermission newPermission : newPermissions) {
            tenantPermissionEventPublisher.publishTenantPermissionCreatedEvent(
                newPermission);
        }

        return newPermissions;
    }

    @Override
    public void delete(Integer id) {
        Optional<TenantPermissionEntity> findEntity =
            permissionRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        permissionRepository.delete(findEntity.get());

        tenantPermissionEventPublisher.publishTenantPermissionDeletedEvent(
            permissionMapper.entityToObject(findEntity.get()));
    }

    @Override
    public TenantPermission getById(Integer id) {
        Optional<TenantPermissionEntity> entity =
            permissionRepository.getById(id);

        if (entity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        return permissionMapper.entityToObject(entity.get());
    }

    @Override
    public List<TenantPermission> getByIdList(List<Integer> idList) {
        List<TenantPermissionEntity> permissionList =
            permissionRepository.getByIdList(idList);

        return permissionMapper.entityToList(permissionList);
    }

    @Override
    public QueryResult<TenantPermission> getList(Query<Integer> query) {
        List<TenantPermissionEntity> entityList =
            permissionRepository.getList(query);

        QueryResult<TenantPermission> result = new QueryResult<>();
        result.setData(permissionMapper.entityToList(entityList));
        result.getMeta().setPageCount(entityList.size());

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        result.getMeta().setCount(permissionRepository.count(query));

        return result;
    }

    @Override
    public QueryResult<TenantPermission> getSingle(Query<Integer> query) {
        Optional<TenantPermissionEntity> entity =
            permissionRepository.getSingle(query);

        if (entity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        QueryResult<TenantPermission> result = new QueryResult<>();
        List<TenantPermission> data = new ArrayList<>();
        data.add(permissionMapper.entityToObject(entity.get()));
        result.setData(data);

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        return result;
    }

    @Override
    public TenantPermission update(Integer id,
                                   TenantPermission permission) {
        TenantPermissionEntity entity = permissionMapper.toEntity(permission);
        entity.setId(id);

        permissionRepository.save(entity);

        TenantPermission updatedPermission =
            permissionMapper.entityToObject(entity);
        tenantPermissionEventPublisher.publishTenantPermissionUpdatedEvent(
            updatedPermission);

        return updatedPermission;
    }

    @Override
    public List<TenantPermission> updateAll(
        List<TenantPermission> permissionList) {
        List<TenantPermissionEntity> entityList =
            permissionMapper.toEntityList(permissionList);

        permissionRepository.saveList(entityList);

        List<TenantPermission> updatedPermissions =
            permissionMapper.entityToList(entityList);
        for (TenantPermission updatedPermission : updatedPermissions) {
            tenantPermissionEventPublisher.publishTenantPermissionUpdatedEvent(
                updatedPermission);
        }

        return updatedPermissions;
    }

    @Override
    public TenantPermission updatePartial(Integer id,
                                          TenantPermission permission) {
        Optional<TenantPermissionEntity> findEntity =
            permissionRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new PermissionNotFoundException();
        }

        TenantPermissionEntity foundEntity = findEntity.get();
        permission.setId(foundEntity.getId());

        permissionMapper.update(foundEntity, permission);
        permissionRepository.save(foundEntity);

        TenantPermission updatedPermission =
            permissionMapper.entityToObject(foundEntity);
        tenantPermissionEventPublisher.publishTenantPermissionUpdatedEvent(
            updatedPermission);

        return updatedPermission;
    }
}