package template.internal.repositories;

import org.springframework.stereotype.Service;
import template.database.models.Query;
import template.database.services.QueryBuilder;
import template.internal.entities.InternalUserEntity;

import java.util.List;
import java.util.Optional;

@Service
public class InternalUserRepositoryImpl
    implements InternalUserRepository {
    private final IInternalUserRepository userRepository;
    private final QueryBuilder<InternalUserEntity, Integer> queryBuilder;

    public InternalUserRepositoryImpl(IInternalUserRepository userRepository,
                                      QueryBuilder<InternalUserEntity, Integer> queryBuilder) {
        this.userRepository = userRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(InternalUserEntity.class, query);
    }

    @Override
    public void delete(InternalUserEntity user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<InternalUserEntity> getById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<InternalUserEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(InternalUserEntity.class, query);
    }

    @Override
    public InternalUserEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public InternalUserEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<InternalUserEntity> getSingle(Query<Integer> query) {
        InternalUserEntity entity =
            queryBuilder.getSingle(InternalUserEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(InternalUserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void saveList(List<InternalUserEntity> entityList) {
        userRepository.saveAllAndFlush(entityList);
    }
}
