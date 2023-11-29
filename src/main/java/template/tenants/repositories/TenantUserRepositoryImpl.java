package template.tenants.repositories;

import org.springframework.stereotype.Service;
import template.database.models.Query;
import template.database.services.QueryBuilder;
import template.tenants.entities.TenantUserEntity;

import java.util.List;
import java.util.Optional;

@Service
public class TenantUserRepositoryImpl
    implements TenantUserRepository {
    private final ITenantUserRepository userRepository;
    private final QueryBuilder<TenantUserEntity, Integer> queryBuilder;

    public TenantUserRepositoryImpl(ITenantUserRepository userRepository,
                                    QueryBuilder<TenantUserEntity, Integer> queryBuilder) {
        this.userRepository = userRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(TenantUserEntity.class, query);
    }

    @Override
    public void delete(TenantUserEntity user) {
        userRepository.delete(user);
    }

    @Override
    public Optional<TenantUserEntity> getById(Integer id) {
        return userRepository.findById(id);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<TenantUserEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(TenantUserEntity.class, query);
    }

    @Override
    public TenantUserEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public TenantUserEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<TenantUserEntity> getSingle(Query<Integer> query) {
        TenantUserEntity entity =
            queryBuilder.getSingle(TenantUserEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(TenantUserEntity user) {
        userRepository.save(user);
    }

    @Override
    public void saveList(List<TenantUserEntity> entityList) {
        userRepository.saveAllAndFlush(entityList);
    }
}
