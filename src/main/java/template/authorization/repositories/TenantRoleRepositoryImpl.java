package template.authorization.repositories;

import org.springframework.stereotype.Service;
import template.authorization.entities.TenantRoleEntity;
import template.database.models.Query;
import template.database.services.QueryBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class TenantRoleRepositoryImpl implements TenantRoleRepository {
    private final ITenantRoleRepository roleRepository;
    private final QueryBuilder<TenantRoleEntity, Integer> queryBuilder;

    public TenantRoleRepositoryImpl(ITenantRoleRepository roleRepository,
                                    QueryBuilder<TenantRoleEntity, Integer> queryBuilder) {
        this.roleRepository = roleRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(TenantRoleEntity.class, query);
    }

    @Override
    public void delete(TenantRoleEntity role) {
        roleRepository.delete(role);
    }

    @Override
    public Optional<TenantRoleEntity> getById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<TenantRoleEntity> getByIdList(List<Integer> ids) {
        return roleRepository.getListByIdIn(ids);
    }

    @Override
    public Optional<TenantRoleEntity> getByName(String name) {
        return roleRepository.getByName(name);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<TenantRoleEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(TenantRoleEntity.class, query);
    }

    @Override
    public List<TenantRoleEntity> getListByUsersId(Integer id) {
        return roleRepository.getListByUsersId(id);
    }

    @Override
    public TenantRoleEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public TenantRoleEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<TenantRoleEntity> getSingle(Query<Integer> query) {
        TenantRoleEntity entity =
            queryBuilder.getSingle(TenantRoleEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(TenantRoleEntity role) {
        roleRepository.save(role);
    }

    @Override
    public void saveList(List<TenantRoleEntity> entityList) {
        roleRepository.saveAllAndFlush(entityList);
    }
}
