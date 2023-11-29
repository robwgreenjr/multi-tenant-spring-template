package template.database.repositories;

import java.util.List;

public interface ListRepository<T, ID> {
    void saveList(List<T> entityList);
}
