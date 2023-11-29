package template.global.services;

import template.database.models.Query;
import template.database.models.QueryResult;

public interface QueryManager<T, ID> {
    QueryResult<T> getList(Query<ID> query);

    QueryResult<T> getSingle(Query<ID> query);
}
