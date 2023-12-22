package template.global.utilities;

import template.database.models.Query;

import java.util.Map;

public interface ParameterProcessor<T> {
    Query<T> buildQuery(Map<String, String[]> queryParams);

    String buildCursorPaginationUrl(String url, String cursorProperty,
                                    String cursorValue);
}