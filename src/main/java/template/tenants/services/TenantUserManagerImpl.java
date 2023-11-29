package template.tenants.services;

import org.springframework.stereotype.Service;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.tenants.entities.TenantUserEntity;
import template.tenants.exceptions.TenantUserNotFoundException;
import template.tenants.mappers.TenantUserMapper;
import template.tenants.models.TenantUser;
import template.tenants.repositories.TenantUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TenantUserManagerImpl implements TenantUserManager {
    private final TenantUserRepository userRepository;
    private final TenantUserMapper userMapper;

    public TenantUserManagerImpl(
        TenantUserRepository userRepository,
        TenantUserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    public TenantUser create(TenantUser user) {
        TenantUserEntity newEntity = userMapper.toEntity(user);

        userRepository.save(newEntity);

        return userMapper.entityToObject(newEntity);
    }

    @Override
    public List<TenantUser> createAll(List<TenantUser> userList) {
        List<TenantUserEntity> newEntityList =
            userMapper.toEntityList(userList);

        userRepository.saveList(newEntityList);

        return userMapper.entityToList(newEntityList);
    }

    @Override
    public void delete(Integer id) {
        Optional<TenantUserEntity> findEntity =
            userRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new TenantUserNotFoundException();
        }

        userRepository.delete(findEntity.get());
    }

    @Override
    public QueryResult<TenantUser> getList(Query<Integer> query) {
        List<TenantUserEntity> entityList =
            userRepository.getList(query);

        QueryResult<TenantUser> result = new QueryResult<>();
        result.setData(userMapper.entityToList(entityList));
        result.getMeta().setPageCount(entityList.size());

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        result.getMeta().setCount(userRepository.count(query));

        return result;
    }

    @Override
    public QueryResult<TenantUser> getSingle(Query<Integer> query) {
        Optional<TenantUserEntity> entity =
            userRepository.getSingle(query);

        if (entity.isEmpty()) {
            throw new TenantUserNotFoundException();
        }

        QueryResult<TenantUser> result = new QueryResult<>();
        List<TenantUser> data = new ArrayList<>();
        data.add(userMapper.entityToObject(entity.get()));
        result.setData(data);

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        return result;
    }

    @Override
    public TenantUser update(Integer id, TenantUser user) {
        TenantUserEntity entity = userMapper.toEntity(user);
        entity.setId(id);

        userRepository.save(entity);

        return user;
    }

    @Override
    public List<TenantUser> updateAll(List<TenantUser> userList) {
        List<TenantUserEntity> entityList =
            userMapper.toEntityList(userList);

        userRepository.saveList(entityList);

        return userMapper.entityToList(entityList);
    }

    @Override
    public TenantUser updatePartial(Integer id, TenantUser user) {
        Optional<TenantUserEntity> findEntity =
            userRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new TenantUserNotFoundException();
        }

        TenantUserEntity foundEntity = findEntity.get();
        user.setId(foundEntity.getId());

        userMapper.update(foundEntity, user);
        userRepository.save(foundEntity);

        user = userMapper.entityToObject(foundEntity);

        return user;
    }
}
