package template.authentication.services;

import java.util.Optional;

public interface UserPasswordManager<T> {
    T create(T userPassword);

    void delete(Integer id);

    Optional<T> findByUserEmail(String email);

    T update(Integer id, T userPassword);

    T updatePartial(Integer id, T userPassword);
}