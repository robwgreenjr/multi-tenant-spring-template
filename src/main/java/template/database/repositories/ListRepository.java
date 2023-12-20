package template.database.repositories;

import java.util.List;

public interface ListRepository<T> {
    void saveList(List<T> entityList);
}
