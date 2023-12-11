package template.database.models;

import template.database.enums.QueryConjunctive;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ColumnFilterList {
    private QueryConjunctive conjunctive;
    private List<ColumnFilter> filters;

    public ColumnFilterList() {
        this.conjunctive = QueryConjunctive.AND;
        this.filters = new ArrayList<>();
    }

    public QueryConjunctive getConjunctive() {
        return conjunctive;
    }

    public void setConjunctive(QueryConjunctive conjunctive) {
        this.conjunctive = conjunctive;
    }

    public List<ColumnFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ColumnFilter> filters) {
        this.filters = filters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnFilterList that = (ColumnFilterList) o;
        return conjunctive == that.conjunctive &&
            Objects.equals(filters, that.filters);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conjunctive, filters);
    }

    @Override
    public String toString() {
        return "ColumnFilterList{" +
            "conjunctive=" + conjunctive +
            ", filters=" + filters +
            '}';
    }
}
