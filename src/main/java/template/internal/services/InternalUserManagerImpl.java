package template.internal.services;

import org.springframework.stereotype.Service;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.internal.entities.InternalUserEntity;
import template.internal.events.publishers.InternalUserEventPublisher;
import template.internal.exceptions.InternalUserNotFoundException;
import template.internal.mappers.InternalUserMapper;
import template.internal.models.InternalUser;
import template.internal.repositories.InternalUserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class InternalUserManagerImpl implements InternalUserManager {
    private final InternalUserRepository userRepository;
    private final InternalUserMapper userMapper;
    private final InternalUserEventPublisher internalUserEventPublisher;

    public InternalUserManagerImpl(
        InternalUserRepository userRepository,
        InternalUserMapper userMapper,
        InternalUserEventPublisher internalUserEventPublisher) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.internalUserEventPublisher = internalUserEventPublisher;
    }

    @Override
    public InternalUser create(InternalUser user) {
        InternalUserEntity newEntity = userMapper.toEntity(user);

        userRepository.save(newEntity);

        InternalUser newUser = userMapper.entityToObject(newEntity);
        internalUserEventPublisher.publishInternalUserCreatedEvent(newUser);

        return newUser;
    }

    @Override
    public List<InternalUser> createAll(List<InternalUser> userList) {
        List<InternalUserEntity> newEntityList =
            userMapper.toEntityList(userList);

        userRepository.saveList(newEntityList);

        List<InternalUser> newUsers = userMapper.entityToList(newEntityList);
        for (InternalUser newUser : newUsers) {
            internalUserEventPublisher.publishInternalUserCreatedEvent(newUser);
        }

        return newUsers;
    }

    @Override
    public void delete(Integer id) {
        Optional<InternalUserEntity> findEntity =
            userRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new InternalUserNotFoundException();
        }

        userRepository.delete(findEntity.get());

        internalUserEventPublisher.publishInternalUserDeletedEvent(
            userMapper.entityToObject(findEntity.get()));
    }

    @Override
    public QueryResult<InternalUser> getList(Query<Integer> query) {
        List<InternalUserEntity> entityList =
            userRepository.getList(query);

        QueryResult<InternalUser> result = new QueryResult<>();
        result.setData(userMapper.entityToList(entityList));
        result.getMeta().setPageCount(entityList.size());

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        result.getMeta().setCount(userRepository.count(query));

        return result;
    }

    @Override
    public QueryResult<InternalUser> getSingle(Query<Integer> query) {
        Optional<InternalUserEntity> entity =
            userRepository.getSingle(query);

        if (entity.isEmpty()) {
            throw new InternalUserNotFoundException();
        }

        QueryResult<InternalUser> result = new QueryResult<>();
        List<InternalUser> data = new ArrayList<>();
        data.add(userMapper.entityToObject(entity.get()));
        result.setData(data);

        if (query.getLimit() != null) {
            result.getMeta().setLimit(query.getLimit());
        }

        return result;
    }

    @Override
    public InternalUser update(Integer id, InternalUser user) {
        InternalUserEntity entity = userMapper.toEntity(user);
        entity.setId(id);

        userRepository.save(entity);

        InternalUser updatedUser = userMapper.entityToObject(entity);
        internalUserEventPublisher.publishInternalUserUpdatedEvent(updatedUser);

        return updatedUser;
    }

    @Override
    public List<InternalUser> updateAll(List<InternalUser> userList) {
        List<InternalUserEntity> entityList =
            userMapper.toEntityList(userList);

        userRepository.saveList(entityList);

        List<InternalUser> updatedUsers = userMapper.entityToList(entityList);
        for (InternalUser updatedUser : updatedUsers) {
            internalUserEventPublisher.publishInternalUserUpdatedEvent(
                updatedUser);
        }

        return updatedUsers;
    }

    @Override
    public InternalUser updatePartial(Integer id, InternalUser user) {
        Optional<InternalUserEntity> findEntity =
            userRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new InternalUserNotFoundException();
        }

        InternalUserEntity foundEntity = findEntity.get();
        user.setId(foundEntity.getId());

        userMapper.update(foundEntity, user);
        userRepository.save(foundEntity);

        InternalUser updatedUser = userMapper.entityToObject(foundEntity);
        internalUserEventPublisher.publishInternalUserUpdatedEvent(updatedUser);

        return updatedUser;
    }
}
