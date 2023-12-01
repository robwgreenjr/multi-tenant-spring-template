package template.authentication.services;

public interface UserPasswordManager<T> {
    T findByUserEmail(String email);

    T create(T userPassword);

    T update(Integer id, T userPassword) throws Exception;

    T updatePartial(Integer id, T userPassword);

    void delete(Integer id);
}