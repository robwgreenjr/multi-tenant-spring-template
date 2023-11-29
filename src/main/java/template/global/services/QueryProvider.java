package template.global.services;

import template.database.models.Query;
import template.database.models.QueryResult;

import java.util.List;

public interface QueryProvider<T, S, R> {
    QueryResult<R> buildQueryResult(Class<T> entity, Query<S> query,
                                    List<T> data,
                                    List<R> mappedData);
}
