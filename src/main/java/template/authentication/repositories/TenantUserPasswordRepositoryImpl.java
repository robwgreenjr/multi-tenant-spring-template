package template.authentication.repositories;

import org.springframework.stereotype.Service;
import template.authentication.entities.TenantUserPasswordEntity;

import java.util.Optional;

@Service
public class TenantUserPasswordRepositoryImpl implements
    TenantUserPasswordRepository {
    private final ITenantUserPasswordRepository userPasswordRepository;

    public TenantUserPasswordRepositoryImpl(
        ITenantUserPasswordRepository userPasswordRepository) {
        this.userPasswordRepository = userPasswordRepository;
    }

    @Override
    public Optional<TenantUserPasswordEntity> getById(Integer id) {
        return userPasswordRepository.findById(id);
    }

    @Override
    public Optional<TenantUserPasswordEntity> getByUserEmail(String email) {
        return userPasswordRepository.getByUserEmail(email);
    }

    @Override
    public void save(TenantUserPasswordEntity userPassword) {
        userPasswordRepository.save(userPassword);
    }

    @Override
    public void delete(TenantUserPasswordEntity userPassword) {
        userPasswordRepository.delete(userPassword);
    }
}
