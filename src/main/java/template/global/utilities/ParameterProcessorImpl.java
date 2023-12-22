package template.global.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import template.database.enums.QueryConjunctive;
import template.database.enums.QueryFilter;
import template.database.enums.QuerySort;
import template.database.models.ColumnFilter;
import template.database.models.ColumnFilterList;
import template.database.models.Query;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ParameterProcessorImpl<T> implements ParameterProcessor<T> {

    @Override
    public Query<T> buildQuery(Map<String, String[]> queryParams) {
        Query<T> query = new Query<>();

        buildFilter(query, queryParams);
        buildSort(query, queryParams);
        buildPagination(query, queryParams);
        
        return query;
    }

    @Override
    public String buildCursorPaginationUrl(String url, String cursorProperty,
                                           String cursorValue) {
        url = URLDecoder.decode(url, StandardCharsets.UTF_8);

        String modifiedUrl = url.split("\\?")[0];
        String parametersOnly = Arrays.stream(url.split("\\?")).skip(1).collect(
            Collectors.joining("&"));

        parametersOnly = Arrays.stream(parametersOnly.split("\\&"))
            // Remove duplicate cursor params
            .filter(parameter -> !URLDecoder.decode(parameter,
                StandardCharsets.UTF_8).contains("[cursor]"))
            .collect(Collectors.joining("&"));

        if (parametersOnly.isEmpty()) {
            parametersOnly += cursorProperty + "[cursor]=" + cursorValue;
        } else {
            parametersOnly += "&" + cursorProperty + "[cursor]=" + cursorValue;
        }

        return modifiedUrl + ("?" + parametersOnly);
    }

    private void buildFilter(Query<T> query,
                             Map<String, String[]> queryParams) {
        List<ColumnFilterList> columnFilterLists = new ArrayList<>();

        queryParams.forEach((key, value) -> {
            key = URLDecoder.decode(key, StandardCharsets.UTF_8);
            if (key.equals("sort_by") || key.equals("limit")) return;

            for (String val : value) {
                ColumnFilterList columnFilterList = new ColumnFilterList();
                if (key.contains("[" + QueryConjunctive.OR + "]")) {
                    columnFilterList.setConjunctive(QueryConjunctive.OR);
                    key = key.replace("[" + QueryConjunctive.OR + "]", "");
                } else if (key.contains(
                    "[" + QueryConjunctive.OR.toString().toLowerCase() + "]")) {
                    columnFilterList.setConjunctive(QueryConjunctive.OR);
                    key = key.replace(
                        "[" + QueryConjunctive.OR.toString().toLowerCase() +
                            "]", "");
                } else if (key.contains("[" + QueryConjunctive.AND + "]")) {
                    key = key.replace("[" + QueryConjunctive.AND + "]", "");
                } else if (key.contains(
                    "[" + QueryConjunctive.AND.toString().toLowerCase() +
                        "]")) {
                    key = key.replace(
                        "[" + QueryConjunctive.AND.toString().toLowerCase() +
                            "]", "");
                }

                extractFilter(columnFilterList.getFilters(), key);
                extractValue(columnFilterList.getFilters(), val);

                columnFilterLists.add(columnFilterList);
            }
        });

        query.setFilterList(columnFilterLists);
    }

    private void buildSort(Query<T> query,
                           Map<String, String[]> queryParams) {
        String[] sort = queryParams.get("sort_by");
        HashMap<QuerySort, String[]> sortedList = new HashMap<>();

        if (sort == null || sort.length != 1) {
            String[] sortedFields = {"id"};

            sortedList.put(QuerySort.ASC, sortedFields);
        } else {
            String[] multipleSorts = sort[0].split("\\),");
            if (multipleSorts.length == 2) {
                multipleSorts[0] += ")";
            }

            for (String sorted : multipleSorts) {
                String sortMethod = sorted.substring(0, sorted.indexOf("("));
                sortMethod = sortMethod.toUpperCase(Locale.ROOT);
                if (!sortMethod.equals(QuerySort.ASC.toString()) &&
                    !sortMethod.equals(QuerySort.DESC.toString()))
                    return;

                String[] sortedFields =
                    sorted.substring(sorted.indexOf("(") + 1,
                            sorted.indexOf(")"))
                        .split(",");

                sortedList.put(QuerySort.valueOf(sortMethod), sortedFields);
            }
        }

        query.setSortList(sortedList);
    }

    private void buildPagination(Query<T> query,
                                 Map<String, String[]> queryParams) {
        String[] limit = queryParams.get("limit");

        if (limit != null && limit.length == 1) {
            Integer limitValue = Integer.parseInt(limit[0]);

            if (limitValue > Query.MAX_LIMIT) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Exceeded the current limit on querying data of: " +
                        Query.MAX_LIMIT);
            }

            query.setLimit(limitValue);
        }
    }

    private void extractFilter(List<ColumnFilter> columnFilterList,
                               String key) {
        ColumnFilter columnFilter = new ColumnFilter();
        columnFilter.setConjunctive(QueryConjunctive.AND);

        if (key.contains(
            "[" + QueryFilter.GTE.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.GTE);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.GTE.toString().toLowerCase() + "]")[0]);
        } else if (key.contains(
            "[" + QueryFilter.GT.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.GT);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.GT.toString().toLowerCase() + "]")[0]);
        } else if (key.contains(
            "[" + QueryFilter.LTE.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.LTE);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.LTE.toString().toLowerCase() + "]")[0]);
        } else if (key.contains(
            "[" + QueryFilter.LT.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.LT);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.LT.toString().toLowerCase() + "]")[0]);
        } else if (key.contains(
            "[" + QueryFilter.LIKE.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.LIKE);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.LIKE.toString().toLowerCase() +
                        "]")[0]);
        } else if (key.contains(
            "[" + QueryFilter.NE.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.NE);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.NE.toString().toLowerCase() + "]")[0]);
        } else if (key.contains(
            "[" + QueryFilter.CURSOR.toString().toLowerCase() + "]")) {
            columnFilter.setFilter(QueryFilter.CURSOR);
            columnFilter.setProperty(
                key.split(
                    "\\[" + QueryFilter.CURSOR.toString().toLowerCase() +
                        "]")[0]);
        } else {
            columnFilter.setFilter(QueryFilter.EQ);
            columnFilter.setProperty(key);
        }

        columnFilterList.add(columnFilterList.size(), columnFilter);
    }

    private void extractValue(List<ColumnFilter> columnFilterList,
                              String value) {
        ColumnFilter columnFilter = columnFilterList.get(0);
        if (columnFilter == null) return;

        String mainValue = "";
        if (!value.contains("[or]") && !value.contains("[and]")) {
            mainValue = value;
        } else if (value.split("\\[and]|\\[or]").length > 1) {
            mainValue = value.split("\\[and]|\\[or]")[0];
        }

        columnFilter.setValue(mainValue);

        String remainingValue = value.substring(mainValue.length());
        if (remainingValue.isEmpty()) return;

        String[] finalValues = remainingValue.split("\\[and]|\\[or]");

        List<HashMap<QueryConjunctive, Integer>> finalConjunctions =
            new ArrayList<>();

        int andPlacements =
            value.indexOf(
                "[" + QueryConjunctive.AND.toString().toLowerCase() + "]");
        int orPlacements = value.indexOf(
            "[" + QueryConjunctive.OR.toString().toLowerCase() + "]");
        while (orPlacements != -1) {
            HashMap<QueryConjunctive, Integer> orPlacement = new HashMap<>();
            orPlacement.put(QueryConjunctive.OR, orPlacements);
            finalConjunctions.add(orPlacement);

            orPlacements = value.indexOf(
                "[" + QueryConjunctive.OR.toString().toLowerCase() + "]",
                orPlacements + 1);
        }

        while (andPlacements != -1) {
            HashMap<QueryConjunctive, Integer> andPlacement = new HashMap<>();
            andPlacement.put(QueryConjunctive.AND, andPlacements);
            finalConjunctions.add(andPlacement);

            andPlacements = value.indexOf(
                "[" + QueryConjunctive.AND.toString().toLowerCase() + "]",
                andPlacements + 1);
        }

        finalConjunctions.sort((a, b) -> {
            Integer first = (Integer) a.values().toArray()[0];
            Integer second = (Integer) b.values().toArray()[0];

            return first.compareTo(second);
        });

        int index = 0;
        for (String finalValue : finalValues) {
            if (finalValue.isEmpty()) continue;
            String[] keyValue = finalValue.split("=");
            if (keyValue.length != 2) continue;

            extractFilter(columnFilterList, keyValue[0]);

            columnFilterList.get(columnFilterList.size() - 1)
                .setValue(keyValue[1]);
            columnFilterList.get(columnFilterList.size() - 1).setConjunctive(
                ((QueryConjunctive) finalConjunctions.get(index).keySet()
                    .toArray()[0]));
            index++;
        }
    }
}