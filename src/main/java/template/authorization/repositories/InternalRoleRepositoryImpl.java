package template.authorization.repositories;

import org.springframework.stereotype.Service;
import template.authorization.entities.InternalRoleEntity;
import template.database.models.Query;
import template.database.services.QueryBuilder;

import java.util.List;
import java.util.Optional;

@Service
public class InternalRoleRepositoryImpl implements InternalRoleRepository {
    private final IInternalRoleRepository roleRepository;
    private final QueryBuilder<InternalRoleEntity, Integer> queryBuilder;

    public InternalRoleRepositoryImpl(IInternalRoleRepository roleRepository,
                                      QueryBuilder<InternalRoleEntity, Integer> queryBuilder) {
        this.roleRepository = roleRepository;
        this.queryBuilder = queryBuilder;
    }

    @Override
    public Integer count(Query<Integer> query) {
        return queryBuilder.getCount(InternalRoleEntity.class, query);
    }

    @Override
    public void delete(InternalRoleEntity role) {
        roleRepository.delete(role);
    }

    @Override
    public Optional<InternalRoleEntity> getById(Integer id) {
        return roleRepository.findById(id);
    }

    @Override
    public List<InternalRoleEntity> getByIdList(List<Integer> ids) {
        return roleRepository.getListByIdIn(ids);
    }

    @Override
    public Optional<InternalRoleEntity> getByName(String name) {
        return roleRepository.getByName(name);
    }

    @Override
    public Integer getCurrentPage(Integer count, Query<Integer> query) {
        return null;
    }

    @Override
    public List<InternalRoleEntity> getList(Query<Integer> query) {
        return queryBuilder.getList(InternalRoleEntity.class, query);
    }

    @Override
    public List<InternalRoleEntity> getListByUsersId(Integer id) {
        return roleRepository.getListByUsersId(id);
    }

    @Override
    public InternalRoleEntity getNext(Query<Integer> query) {
        return null;
    }

    @Override
    public InternalRoleEntity getPrevious(Query<Integer> query) {
        return null;
    }

    @Override
    public Optional<InternalRoleEntity> getSingle(Query<Integer> query) {
        InternalRoleEntity entity =
            queryBuilder.getSingle(InternalRoleEntity.class, query);

        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    @Override
    public void save(InternalRoleEntity role) {
        roleRepository.save(role);
    }

    @Override
    public void saveList(List<InternalRoleEntity> entityList) {
        roleRepository.saveAllAndFlush(entityList);
    }
}
