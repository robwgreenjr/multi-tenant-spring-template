package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.InternalUserPasswordEntity;
import template.authentication.events.publishers.InternalUserPasswordEventPublisher;
import template.authentication.exceptions.UserPasswordCreateIncompleteException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.exceptions.UserPasswordUpdateIncompleteException;
import template.authentication.mappers.InternalUserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.repositories.InternalUserPasswordRepository;

import java.util.Objects;
import java.util.Optional;

@Service("InternalUserPasswordManager")
public class InternalUserPasswordManager
    implements UserPasswordManager<InternalUserPassword> {
    private final InternalUserPasswordRepository userPasswordRepository;
    private final InternalUserPasswordMapper userPasswordMapper;
    private final InternalUserPasswordEventPublisher userPasswordEventPublisher;

    public InternalUserPasswordManager(
        InternalUserPasswordRepository userPasswordRepository,
        InternalUserPasswordMapper userPasswordMapper,
        InternalUserPasswordEventPublisher userPasswordEventPublisher) {
        this.userPasswordRepository = userPasswordRepository;
        this.userPasswordMapper = userPasswordMapper;
        this.userPasswordEventPublisher = userPasswordEventPublisher;
    }

    @Override
    public InternalUserPassword findByUserEmail(String email) {
        Optional<InternalUserPasswordEntity> userPassword =
            userPasswordRepository.getByUserEmail(email);

        if (userPassword.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        return userPasswordMapper.entityToObject(userPassword.get());
    }

    @Override
    public InternalUserPassword create(InternalUserPassword userPasswordModel) {
        InternalUserPasswordEntity newUserPassword =
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
    public InternalUserPassword update(Integer id,
                                       InternalUserPassword userPasswordModel)
        throws Exception {
        InternalUserPasswordEntity entity =
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
    public InternalUserPassword updatePartial(Integer id,
                                              InternalUserPassword userPasswordModel) {
        Optional<InternalUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        InternalUserPasswordEntity foundEntity = findEntity.get();
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
        Optional<InternalUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        userPasswordRepository.delete(findEntity.get());

        InternalUserPassword userPasswordModel =
            userPasswordMapper.entityToObject(findEntity.get());
        userPasswordEventPublisher.publishUserPasswordDeletedEvent(
            userPasswordModel);
    }
}