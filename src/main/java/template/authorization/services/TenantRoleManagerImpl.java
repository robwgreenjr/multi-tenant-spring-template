package template.authorization.services;

import org.springframework.stereotype.Service;
import template.authorization.entities.TenantRoleEntity;
import template.authorization.exceptions.RoleNotFoundException;
import template.authorization.mappers.TenantRoleMapper;
import template.authorization.models.TenantRole;
import template.authorization.repositories.TenantRoleRepository;
import template.database.models.Query;
import template.database.models.QueryResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TenantRoleManagerImpl implements TenantRoleManager {
    private final TenantRoleRepository roleRepository;
    private final TenantRoleMapper roleMapper;


    public TenantRoleManagerImpl(TenantRoleRepository roleRepository,
                                 TenantRoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    @Override
    public QueryResult<TenantRole> getList(Query<Integer> query) {
        List<TenantRoleEntity> entityList =
            roleRepository.getList(query);

        QueryResult<TenantRole> result = new QueryResult<>();
        result.setData(roleMapper.entityToList(entityList));
        result.getMeta().setPageCount(entityList.size());

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        result.getMeta().setCount(roleRepository.count(query));

        return result;
    }

    @Override
    public List<TenantRole> getListByUserId(Integer id) {
        List<TenantRoleEntity> entityList =
            roleRepository.getListByUsersId(id);

        return roleMapper.entityToList(entityList);
    }

    @Override
    public TenantRole getByName(String name) {
        Optional<TenantRoleEntity> entity = roleRepository.getByName(name);

        if (entity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return roleMapper.entityToObject(entity.get());
    }

    @Override
    public List<TenantRole> getByIdList(List<Integer> ids) {
        List<TenantRoleEntity> entityList = roleRepository.getByIdList(ids);

        return roleMapper.entityToList(entityList);
    }


    @Override
    public TenantRole getById(Integer id) {
        Optional<TenantRoleEntity> entity = roleRepository.getById(id);

        if (entity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        return roleMapper.entityToObject(entity.get());
    }

    @Override
    public QueryResult<TenantRole> getSingle(Query<Integer> query) {
        Optional<TenantRoleEntity> entity =
            roleRepository.getSingle(query);

        if (entity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        QueryResult<TenantRole> result = new QueryResult<>();
        List<TenantRole> data = new ArrayList<>();
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
    public TenantRole create(TenantRole role) {
        TenantRoleEntity newEntity = roleMapper.toEntity(role);

        roleRepository.save(newEntity);

        return roleMapper.entityToObject(newEntity);
    }

    @Override
    public List<TenantRole> createAll(List<TenantRole> roleList) {
        List<TenantRoleEntity> entityList = roleMapper.toEntityList(roleList);

        roleRepository.saveList(entityList);

        return roleMapper.entityToList(entityList);
    }

    @Override
    public void delete(Integer id) {
        Optional<TenantRoleEntity> findEntity = roleRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        roleRepository.delete(findEntity.get());
    }

    @Override
    public TenantRole update(Integer id, TenantRole role) {
        TenantRoleEntity entity = roleMapper.toEntity(role);
        entity.setId(id);

        roleRepository.save(entity);

        return role;
    }

    @Override
    public List<TenantRole> updateAll(List<TenantRole> roleList) {
        List<TenantRoleEntity> entityList = roleMapper.toEntityList(roleList);

        roleRepository.saveList(entityList);

        List<TenantRole> result = roleMapper.entityToList(entityList);

        return roleMapper.entityToList(entityList);
    }

    @Override
    public TenantRole updatePartial(Integer id, TenantRole role) {
        Optional<TenantRoleEntity> findEntity = roleRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new RoleNotFoundException();
        }

        TenantRoleEntity foundEntity = findEntity.get();
        role.setId(foundEntity.getId());

        roleMapper.update(foundEntity, role);
        roleRepository.save(foundEntity);

        return roleMapper.entityToObject(foundEntity);
    }
}