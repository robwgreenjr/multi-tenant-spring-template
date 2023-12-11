package template.database.models;

import template.database.enums.QueryConjunctive;
import template.database.enums.QueryFilter;
import template.database.enums.QuerySort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Query<ID> {
    public static final Integer MAX_LIMIT = 200;
    private List<ColumnFilterList> filterList;
    private HashMap<QuerySort, String[]> sortList;
    private Integer limit;

    public Query() {
        filterList = new ArrayList<>();
        sortList = new HashMap<>();
        limit = getLimit();
    }

    public Query(Query<ID> query) {
        List<ColumnFilterList> copiedFilterList = new ArrayList<>();
        for (ColumnFilterList columnFilterList : query.getFilterList()) {
            ColumnFilterList copiedColumnFilterList = new ColumnFilterList();
            List<ColumnFilter> copiedColumnFilters = new ArrayList<>();
            copiedColumnFilterList.setFilters(copiedColumnFilters);

            for (ColumnFilter columnFilter : columnFilterList.getFilters()) {
                copiedColumnFilterList.getFilters().add(columnFilter);
            }

            copiedFilterList.add(copiedColumnFilterList);
        }

        filterList = copiedFilterList;
        sortList = new HashMap<>(query.getSortList());
        limit = query.getLimit();
    }

    public Query(Query<ID> query, HashMap<QuerySort, String[]> sortList) {
        filterList = query.getFilterList();
        this.sortList = sortList;
        limit = query.getLimit();
    }

    public List<ColumnFilterList> getFilterList() {
        return filterList;
    }

    public void setFilterList(
        List<ColumnFilterList> filterList) {
        this.filterList = filterList;
    }

    public HashMap<QuerySort, String[]> getSortList() {
        return sortList;
    }

    public void setSortList(HashMap<QuerySort, String[]> sortList) {
        this.sortList = sortList;
    }

    public Integer getLimit() {
        if (limit == null) {
            return MAX_LIMIT;
        }

        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "Query{" +
            "filterList=" + filterList +
            ", sortList=" + sortList +
            ", limit=" + limit +
            '}';
    }

    public void setPrimaryId(ID id) {
        if (filterList == null) {
            filterList = new ArrayList<>();
        }

        ColumnFilterList columnFilterList = new ColumnFilterList();

        List<ColumnFilter> columnFilters = new ArrayList<>();
        ColumnFilter columnFilter = new ColumnFilter();
        columnFilter.setConjunctive(QueryConjunctive.AND);
        columnFilter.setFilter(QueryFilter.EQ);
        columnFilter.setProperty("id");
        columnFilter.setValue(id.toString());
        columnFilters.add(columnFilter);
        columnFilterList.setFilters(columnFilters);

        filterList.add(filterList.size(), columnFilterList);
    }

    public void removeIdCursor() {
        for (ColumnFilterList columnFilterList : filterList) {
            columnFilterList.getFilters().removeIf(
                columnFilter -> columnFilter.getProperty().equals("id"));
        }

        filterList.removeIf(
            columnFilterList -> columnFilterList.getFilters().isEmpty());
    }

    public void removeCursor() {
        for (ColumnFilterList columnFilterList : filterList) {
            columnFilterList.getFilters().removeIf(
                columnFilter -> columnFilter.getFilter()
                    .equals(QueryFilter.CURSOR));
        }

        filterList.removeIf(
            columnFilterList -> columnFilterList.getFilters().isEmpty());
    }

    public void setLessThan(String property, ID id) {
        if (filterList == null) {
            filterList = new ArrayList<>();
        }

        ColumnFilterList columnFilterList = new ColumnFilterList();

        List<ColumnFilter> columnFilters = new ArrayList<>();
        ColumnFilter columnFilter = new ColumnFilter();
        columnFilter.setConjunctive(QueryConjunctive.AND);
        columnFilter.setFilter(QueryFilter.LT);
        columnFilter.setProperty(property);
        columnFilter.setValue(id.toString());

        columnFilters.add(columnFilter);
        columnFilterList.setFilters(columnFilters);

        filterList.add(filterList.size(), columnFilterList);
    }

    public Boolean isCursorSet() {
        boolean isCursorSet = false;

        for (ColumnFilterList columnFilterList : filterList) {
            for (ColumnFilter filter : columnFilterList.getFilters()) {
                if (filter.getFilter().equals(QueryFilter.CURSOR)) {
                    isCursorSet = true;

                    break;
                }
            }
        }

        return isCursorSet;
    }
}
