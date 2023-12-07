package template.database.repositories;

import template.database.models.Query;
import template.database.models.QueryResult;

import java.util.Optional;

public interface QueryRepository<T, ID> {
    Integer count(Query<ID> query);

    Integer getCurrentPage(Integer count, Query<ID> query);

    QueryResult<T> getList(Query<ID> query);

    T getNext(Query<ID> query);

    T getPrevious(Query<ID> query);

    Optional<T> getSingle(Query<ID> query);
}
