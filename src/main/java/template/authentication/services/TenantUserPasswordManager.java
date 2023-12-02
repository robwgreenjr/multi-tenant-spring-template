package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.TenantUserPasswordEntity;
import template.authentication.events.publishers.TenantUserPasswordEventPublisher;
import template.authentication.exceptions.UserPasswordCreateIncompleteException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.exceptions.UserPasswordUpdateIncompleteException;
import template.authentication.mappers.TenantUserPasswordMapper;
import template.authentication.models.TenantUserPassword;
import template.authentication.repositories.TenantUserPasswordRepository;

import java.util.Objects;
import java.util.Optional;

@Service("TenantUserPasswordManager")
public class TenantUserPasswordManager
    implements UserPasswordManager<TenantUserPassword> {
    private final TenantUserPasswordRepository userPasswordRepository;
    private final TenantUserPasswordMapper userPasswordMapper;
    private final TenantUserPasswordEventPublisher userPasswordEventPublisher;

    public TenantUserPasswordManager(
        TenantUserPasswordRepository userPasswordRepository,
        TenantUserPasswordMapper userPasswordMapper,
        TenantUserPasswordEventPublisher userPasswordEventPublisher) {
        this.userPasswordRepository = userPasswordRepository;
        this.userPasswordMapper = userPasswordMapper;
        this.userPasswordEventPublisher = userPasswordEventPublisher;
    }

    @Override
    public TenantUserPassword findByUserEmail(String email) {
        Optional<TenantUserPasswordEntity> userPasswordEntity =
            userPasswordRepository.getByUserEmail(email);

        if (userPasswordEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        return userPasswordMapper.entityToObject(userPasswordEntity.get());
    }

    @Override
    public TenantUserPassword create(TenantUserPassword userPasswordModel) {
        TenantUserPasswordEntity newUserPassword =
            userPasswordMapper.toEntity(userPasswordModel);
        try {
            userPasswordRepository.save(newUserPassword);
        } catch (Exception exception) {
            if (Objects.requireNonNull(exception.getMessage())
                .contains("user_id")) {
                throw new UserPasswordCreateIncompleteException(
                    "To create a password a user must be associated with said password.");
            }
        }

        userPasswordModel =
            userPasswordMapper.entityToObject(newUserPassword);
        userPasswordEventPublisher.publishUserPasswordCreatedEvent(
            userPasswordModel);

        return userPasswordModel;
    }

    @Override
    public TenantUserPassword update(Integer id,
                                     TenantUserPassword userPasswordModel)
        throws Exception {
        TenantUserPasswordEntity entity =
            userPasswordMapper.toEntity(userPasswordModel);
//        entity.setId(id);

        try {
            userPasswordRepository.save(entity);
        } catch (Exception exception) {
            if (Objects.requireNonNull(exception.getMessage())
                .contains("user_id")) {
                throw new UserPasswordUpdateIncompleteException(
                    "A user must be associated with a password to update.");
            }
        }

        userPasswordModel = userPasswordMapper.entityToObject(entity);
        userPasswordEventPublisher.publishUserPasswordUpdatedEvent(
            userPasswordModel);

        return userPasswordModel;
    }

    @Override
    public TenantUserPassword updatePartial(Integer id,
                                            TenantUserPassword userPasswordModel) {
        Optional<TenantUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        TenantUserPasswordEntity foundEntity = findEntity.get();
//        userPasswordModel.setId(foundEntity.getId());

        userPasswordMapper.update(foundEntity, userPasswordModel);
        userPasswordRepository.save(foundEntity);

        userPasswordModel = userPasswordMapper.entityToObject(foundEntity);
        userPasswordEventPublisher.publishUserPasswordUpdatedEvent(
            userPasswordModel);

        return userPasswordModel;
    }

    @Override
    public void delete(Integer id) {
        Optional<TenantUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        userPasswordRepository.delete(findEntity.get());

        TenantUserPassword userPasswordModel =
            userPasswordMapper.entityToObject(findEntity.get());
        userPasswordEventPublisher.publishUserPasswordDeletedEvent(
            userPasswordModel);
    }
}