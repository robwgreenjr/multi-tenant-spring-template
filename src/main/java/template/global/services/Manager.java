package template.global.services;

public interface Manager<T, ID> {
    T create(T model);

    void delete(ID id);

    T update(ID id, T model);

    T updatePartial(ID id, T model);
}
