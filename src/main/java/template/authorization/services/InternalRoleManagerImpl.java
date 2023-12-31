package template.authorization.services;

import org.springframework.stereotype.Service;
import template.authorization.entities.InternalRoleEntity;
import template.authorization.events.publishers.InternalRoleEventPublisher;
import template.authorization.exceptions.RoleNotFoundException;
import template.authorization.mappers.InternalRoleMapper;
import template.authorization.models.InternalRole;
import template.authorization.repositories.InternalRoleRepository;
import template.database.models.Query;
import template.database.models.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InternalRoleManagerImpl implements InternalRoleManager {
    private final InternalRoleRepository roleRepository;
    private final InternalRoleMapper roleMapper;
    private final InternalRoleEventPublisher internalRoleEventPublisher;

    public InternalRoleManagerImpl(InternalRoleRepository roleRepository,
                                   InternalRoleMapper roleMapper,
                                   InternalRoleEventPublisher internalRoleEventPublisher) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
        this.internalRoleEventPublisher = internalRoleEventPublisher;
    }

    @Override
    public InternalRole create(InternalRole role) {
        InternalRoleEntity newEntity = roleMapper.toEntity(role);

        roleRepository.save(newEntity);

        InternalRole newRole =
            roleMapper.entityToObject(newEntity);
        internalRoleEventPublisher.publishInternalRoleCreatedEvent(
            newRole);

        return newRole;
    }

    @Override
    public List<InternalRole> createAll(List<InternalRole> roleList) {
        List<InternalRoleEntity> newEntityList =
            roleMapper.toEntityList(roleList);

        roleRepository.saveList(newEntityList);

        List<InternalRole> newRoles =
            roleMapper.entityToList(newEntityList);
        for (InternalRole newRole : newRoles) {
            internalRoleEventPublisher.publishInternalRoleCreatedEvent(
                newRole);
        }

        return newRoles;
    }

    @Override
    public void delete(Integer id) {
        Optional<InternalRoleEntity> findEntity = roleRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        roleRepository.delete(findEntity.get());

        internalRoleEventPublisher.publishInternalRoleDeletedEvent(
            roleMapper.entityToObject(findEntity.get()));
    }

    @Override
    public InternalRole getById(Integer id) {
        Optional<InternalRoleEntity> entity = roleRepository.getById(id);

        if (entity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return roleMapper.entityToObject(entity.get());
    }

    @Override
    public List<InternalRole> getByIdList(List<Integer> ids) {
        List<InternalRoleEntity> entityList = roleRepository.getByIdList(ids);

        return roleMapper.entityToList(entityList);
    }

    @Override
    public InternalRole getByName(String name) {
        Optional<InternalRoleEntity> entity = roleRepository.getByName(name);

        if (entity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return roleMapper.entityToObject(entity.get());
    }

    @Override
    public QueryResult<InternalRole> getList(Query<Integer> query) {
        QueryResult<InternalRoleEntity> entityList =
            roleRepository.getList(query);

        return entityList.mapData(
            roleMapper.entityToList(entityList.getData()));
    }

    @Override
    public List<InternalRole> getListByUserId(Integer id) {
        List<InternalRoleEntity> entityList =
            roleRepository.getListByUsersId(id);

        return roleMapper.entityToList(entityList);
    }

    @Override
    public QueryResult<InternalRole> getSingle(Query<Integer> query) {
        Optional<InternalRoleEntity> entity =
            roleRepository.getSingle(query);

        if (entity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        QueryResult<InternalRole> result = new QueryResult<>();
        List<InternalRole> data = new ArrayList<>();
        data.add(roleMapper.entityToObject(entity.get()));
        result.setData(data);
        result.getMeta().setCount(1);
        result.getMeta().setPageCount(1);

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        return result;
    }

    @Override
    public InternalRole update(Integer id, InternalRole role) {
        InternalRoleEntity entity = roleMapper.toEntity(role);
        entity.setId(id);

        roleRepository.save(entity);

        InternalRole updatedRole =
            roleMapper.entityToObject(entity);
        internalRoleEventPublisher.publishInternalRoleUpdatedEvent(
            updatedRole);

        return updatedRole;
    }

    @Override
    public List<InternalRole> updateAll(List<InternalRole> roleList) {
        List<InternalRoleEntity> entityList = roleMapper.toEntityList(roleList);

        roleRepository.saveList(entityList);

        List<InternalRole> updatedRoles = roleMapper.entityToList(entityList);
        for (InternalRole updatedRole : updatedRoles) {
            internalRoleEventPublisher.publishInternalRoleUpdatedEvent(
                updatedRole);
        }

        return updatedRoles;
    }

    @Override
    public InternalRole updatePartial(Integer id, InternalRole role) {
        Optional<InternalRoleEntity> findEntity = roleRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        InternalRoleEntity foundEntity = findEntity.get();
        role.setId(foundEntity.getId());

        roleMapper.update(foundEntity, role);
        roleRepository.save(foundEntity);

        InternalRole updatedRole =
            roleMapper.entityToObject(foundEntity);
        internalRoleEventPublisher.publishInternalRoleUpdatedEvent(
            updatedRole);

        return updatedRole;
    }
}