package template.global.services;

import org.springframework.stereotype.Service;
import template.database.models.Query;
import template.database.models.QueryResult;
import template.database.services.QueryBuilder;

import java.util.List;

@Service
public class QueryProviderImpl<T, S, R> implements QueryProvider<T, S, R> {
    private final QueryBuilder<T, S> queryBuilder;

    public QueryProviderImpl(QueryBuilder<T, S> queryBuilder) {
        this.queryBuilder = queryBuilder;
    }

    @Override
    public QueryResult<R> buildQueryResult(Class<T> entity,
                                           Query<S> query,
                                           List<T> data,
                                           List<R> mappedData) {
        QueryResult<R> result = new QueryResult<>();
        result.setData(mappedData);
        result.getMeta().setLimit(query.getLimit());
        result.getMeta().setCount(queryBuilder.getCount(entity, query));
        result.getMeta().setCursor("id");
        result.getMeta().setPage(
            queryBuilder.getCurrentPage(entity, query,
                result.getMeta().getCount()));
        result.getMeta()
            .setNext(queryBuilder.getNextCursor(entity, query));
        result.getMeta()
            .setPrevious(
                queryBuilder.getPreviousCursor(entity, query, data));
        result.getMeta().setLimit(query.getLimit());

        return result;
    }
}
