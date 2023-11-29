package template.database.repositories;

public interface Repository<T> {
    void delete(T entity);

    void save(T entity);
}
