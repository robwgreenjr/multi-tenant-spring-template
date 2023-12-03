package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.InternalUserPasswordEntity;
import template.authentication.events.publishers.InternalUserPasswordEventPublisher;
import template.authentication.exceptions.UserPasswordInvalidException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.exceptions.UserPasswordUpdateIncompleteException;
import template.authentication.mappers.InternalUserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.repositories.InternalUserPasswordRepository;
import template.internal.entities.InternalUserEntity;
import template.internal.repositories.InternalUserRepository;

import java.util.Objects;
import java.util.Optional;

@Service("InternalUserPasswordManager")
public class InternalUserPasswordManager
    implements UserPasswordManager<InternalUserPassword> {
    private final InternalUserPasswordRepository userPasswordRepository;
    private final InternalUserPasswordMapper userPasswordMapper;
    private final InternalUserPasswordEventPublisher userPasswordEventPublisher;
    private final InternalUserRepository userRepository;

    public InternalUserPasswordManager(
        InternalUserPasswordRepository userPasswordRepository,
        InternalUserPasswordMapper userPasswordMapper,
        InternalUserPasswordEventPublisher userPasswordEventPublisher,
        InternalUserRepository userRepository) {
        this.userPasswordRepository = userPasswordRepository;
        this.userPasswordMapper = userPasswordMapper;
        this.userPasswordEventPublisher = userPasswordEventPublisher;
        this.userRepository = userRepository;
    }

    @Override
    public InternalUserPassword create(InternalUserPassword userPassword) {
        InternalUserPasswordEntity newUserPassword =
            userPasswordMapper.toEntity(userPassword);

        if (newUserPassword.getUser() == null) {
            throw new UserPasswordInvalidException(
                "User is required with new passwords.");
        }

        Optional<InternalUserEntity> findUser =
            userRepository.getById(newUserPassword.getUser().getId());

        if (findUser.isEmpty()) {
            throw new UserPasswordInvalidException(
                "No user found for new password.");
        } else {
            newUserPassword.setUser(findUser.get());
        }

        userPasswordRepository.save(newUserPassword);

        userPassword = userPasswordMapper.entityToObject(newUserPassword);
        userPasswordEventPublisher.publishUserPasswordCreatedEvent(
            userPassword);

        return userPassword;
    }

    @Override
    public void delete(Integer id) {
        Optional<InternalUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        userPasswordRepository.delete(findEntity.get());

        InternalUserPassword userPassword =
            userPasswordMapper.entityToObject(findEntity.get());
        userPasswordEventPublisher.publishUserPasswordDeletedEvent(
            userPassword);
    }

    @Override
    public Optional<InternalUserPassword> findByUserEmail(String email) {
        Optional<InternalUserPasswordEntity> userPassword =
            userPasswordRepository.getByUserEmail(email);

        if (userPassword.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        return Optional.of(
            userPasswordMapper.entityToObject(userPassword.get()));
    }

    @Override
    public InternalUserPassword update(Integer id,
                                       InternalUserPassword userPassword) {
        InternalUserPasswordEntity entity =
            userPasswordMapper.toEntity(userPassword);
        entity.setId(id);

        try {
            userPasswordRepository.save(entity);
        } catch (Exception exception) {
            if (Objects.requireNonNull(exception.getMessage())
                .contains("user_id")) {
                throw new UserPasswordUpdateIncompleteException(
                    "A user must be associated with a password to update.");
            }
        }

        userPassword = userPasswordMapper.entityToObject(entity);
        userPasswordEventPublisher.publishUserPasswordUpdatedEvent(
            userPassword);

        return userPassword;
    }

    @Override
    public InternalUserPassword updatePartial(Integer id,
                                              InternalUserPassword userPassword) {
        Optional<InternalUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        InternalUserPasswordEntity foundEntity = findEntity.get();
        userPassword.setId(foundEntity.getId());

        userPasswordMapper.update(foundEntity, userPassword);
        userPasswordRepository.save(foundEntity);

        userPassword = userPasswordMapper.entityToObject(foundEntity);
        userPasswordEventPublisher.publishUserPasswordUpdatedEvent(
            userPassword);

        return userPassword;
    }
}