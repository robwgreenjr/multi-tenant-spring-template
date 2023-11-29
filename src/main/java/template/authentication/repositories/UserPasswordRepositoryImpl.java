package template.authentication.repositories;

import org.springframework.stereotype.Service;
import template.authentication.entities.InternalUserPasswordEntity;

import java.util.Optional;

@Service
public class UserPasswordRepositoryImpl implements UserPasswordRepository {
    private final IUserPasswordRepository userPasswordRepository;

    public UserPasswordRepositoryImpl(
        IUserPasswordRepository userPasswordRepository) {
        this.userPasswordRepository = userPasswordRepository;
    }

    @Override
    public Optional<InternalUserPasswordEntity> getById(Integer id) {
        return userPasswordRepository.findById(id);
    }

    @Override
    public Optional<InternalUserPasswordEntity> getByUserEmail(String email) {
        return userPasswordRepository.getByUserEmail(email);
    }

    @Override
    public void save(InternalUserPasswordEntity userPassword) {
        userPasswordRepository.save(userPassword);
    }

    @Override
    public void delete(InternalUserPasswordEntity userPassword) {
        userPasswordRepository.delete(userPassword);
    }
}
