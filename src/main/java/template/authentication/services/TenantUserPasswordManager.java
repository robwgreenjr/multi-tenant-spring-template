package template.authentication.services;

import org.springframework.stereotype.Service;
import template.authentication.entities.TenantUserPasswordEntity;
import template.authentication.events.publishers.TenantUserPasswordEventPublisher;
import template.authentication.exceptions.UserPasswordInvalidException;
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
    public TenantUserPassword create(TenantUserPassword userPassword) {
        TenantUserPasswordEntity newUserPassword =
            userPasswordMapper.toEntity(userPassword);
        try {
            userPasswordRepository.save(newUserPassword);
        } catch (Exception exception) {
            if (Objects.requireNonNull(exception.getMessage())
                .contains("user_id")) {
                throw new UserPasswordInvalidException(
                    "To create a password a user must be associated with said password.");
            }
        }

        userPassword =
            userPasswordMapper.entityToObject(newUserPassword);
        userPasswordEventPublisher.publishUserPasswordCreatedEvent(
            userPassword);

        return userPassword;
    }

    @Override
    public void delete(Integer id) {
        Optional<TenantUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        userPasswordRepository.delete(findEntity.get());

        TenantUserPassword userPassword =
            userPasswordMapper.entityToObject(findEntity.get());
        userPasswordEventPublisher.publishUserPasswordDeletedEvent(
            userPassword);
    }

    @Override
    public Optional<TenantUserPassword> findByUserEmail(String email) {
        Optional<TenantUserPasswordEntity> userPasswordEntity =
            userPasswordRepository.getByUserEmail(email);

        if (userPasswordEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        return Optional.of(
            userPasswordMapper.entityToObject(userPasswordEntity.get()));
    }

    @Override
    public TenantUserPassword update(Integer id,
                                     TenantUserPassword userPassword) {
        TenantUserPasswordEntity entity =
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
    public TenantUserPassword updatePartial(Integer id,
                                            TenantUserPassword userPassword) {
        Optional<TenantUserPasswordEntity> findEntity =
            userPasswordRepository.getById(id);

        if (findEntity.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        TenantUserPasswordEntity foundEntity = findEntity.get();
        userPassword.setId(foundEntity.getId());

        userPasswordMapper.update(foundEntity, userPassword);
        userPasswordRepository.save(foundEntity);

        userPassword = userPasswordMapper.entityToObject(foundEntity);
        userPasswordEventPublisher.publishUserPasswordUpdatedEvent(
            userPassword);

        return userPassword;
    }
}