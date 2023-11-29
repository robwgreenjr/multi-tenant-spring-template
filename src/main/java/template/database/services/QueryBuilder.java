package template.database.services;

import template.database.models.Query;

import java.util.List;

public interface QueryBuilder<T, S> {
    T getSingle(Class<T> entity, Query<S> query);

    List<T> getList(Class<T> entity, Query<S> query);

    Integer getCount(Class<T> entity, Query<S> query);

    Integer getCurrentPage(Class<T> entity, Query<S> query,
                           Integer count);

    Integer getNextCursor(Class<T> entity, Query<S> query);

    Integer getPreviousCursor(Class<T> entity, Query<S> query,
                              List<T> data);
}
