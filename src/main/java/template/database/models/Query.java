package template.database.models;

import template.database.enums.QueryConjunctive;
import template.database.enums.QueryFilter;
import template.database.enums.QuerySort;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Query<ID> {
    public static final Integer MAX_LIMIT = 200;
    private List<List<ColumnFilter>> filterList;
    private HashMap<QuerySort, String[]> sortList;
    private Integer limit;

    public Query() {
        this.filterList = new ArrayList<>();
        this.sortList = new HashMap<>();
        this.limit = getLimit();
    }

    public Query(Query<ID> query) {
        List<List<ColumnFilter>> copiedFilterList = new ArrayList<>();
        for (List<ColumnFilter> columnFilterList : query.getFilterList()) {
            copiedFilterList.add(new ArrayList<>(columnFilterList));
        }

        this.filterList = copiedFilterList;
        this.sortList = new HashMap<>(query.getSortList());
        this.limit = query.getLimit();
    }

    public Query(Query<ID> query, HashMap<QuerySort, String[]> sortList) {
        this.filterList = query.getFilterList();
        this.sortList = sortList;
        this.limit = query.getLimit();
    }

    public List<List<ColumnFilter>> getFilterList() {
        return filterList;
    }

    public void setFilterList(List<List<ColumnFilter>> filterList) {
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

        List<ColumnFilter> columnFilterList = new ArrayList<>();
        ColumnFilter columnFilter = new ColumnFilter();
        columnFilter.setConjunctive(QueryConjunctive.AND);
        columnFilter.setFilter(QueryFilter.EQ);
        columnFilter.setProperty("id");
        columnFilter.setValue(id.toString());

        columnFilterList.add(columnFilter);

        filterList.add(filterList.size(), columnFilterList);
    }

    public void removeIdCursor() {
        for (List<ColumnFilter> filters : this.filterList) {
            filters.removeIf(
                columnFilter -> columnFilter.getProperty().equals("id"));
        }

        this.filterList.removeIf(List::isEmpty);
    }

    public void removeCursor() {
        for (List<ColumnFilter> filters : this.filterList) {
            filters.removeIf(
                columnFilter -> columnFilter.getFilter()
                    .equals(QueryFilter.CURSOR));
        }

        this.filterList.removeIf(List::isEmpty);
    }

    public void setLessThan(String property, ID id) {
        if (filterList == null) {
            filterList = new ArrayList<>();
        }

        List<ColumnFilter> columnFilterList = new ArrayList<>();
        ColumnFilter columnFilter = new ColumnFilter();
        columnFilter.setConjunctive(QueryConjunctive.AND);
        columnFilter.setFilter(QueryFilter.LT);
        columnFilter.setProperty(property);
        columnFilter.setValue(id.toString());

        columnFilterList.add(columnFilter);

        filterList.add(filterList.size(), columnFilterList);
    }

    public Boolean isCursorSet() {
        boolean isCursorSet = false;

        for (List<ColumnFilter> filters : this.filterList) {
            for (ColumnFilter filter : filters) {
                if (filter.getFilter().equals(QueryFilter.CURSOR)) {
                    isCursorSet = true;

                    break;
                }
            }
        }

        return isCursorSet;
    }
}
