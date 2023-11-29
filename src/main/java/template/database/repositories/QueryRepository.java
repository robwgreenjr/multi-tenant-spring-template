package template.database.repositories;

import template.database.models.Query;

import java.util.List;
import java.util.Optional;

public interface QueryRepository<T, ID> {
    Integer count(Query<ID> query);

    Integer getCurrentPage(Integer count, Query<ID> query);

    List<T> getList(Query<ID> query);

    T getNext(Query<ID> query);

    T getPrevious(Query<ID> query);

    Optional<T> getSingle(Query<ID> query);
}
