package template.database.models;

import template.database.enums.QueryConjunctive;
import template.database.enums.QueryFilter;

import java.util.Objects;

public class ColumnFilter {
    private QueryConjunctive conjunctive;
    private QueryFilter filter;
    private String property;
    private String value;

    public QueryConjunctive getConjunctive() {
        return conjunctive;
    }

    public void setConjunctive(QueryConjunctive conjunctive) {
        this.conjunctive = conjunctive;
    }

    public QueryFilter getFilter() {
        return filter;
    }

    public void setFilter(QueryFilter filter) {
        this.filter = filter;
    }

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ColumnFilter that = (ColumnFilter) o;
        return conjunctive == that.conjunctive && filter == that.filter &&
            Objects.equals(property, that.property) &&
            Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(conjunctive, filter, property, value);
    }

    @Override
    public String toString() {
        return "ColumnFilter{" +
            "conjunctive=" + conjunctive +
            ", filter=" + filter +
            ", property='" + property + '\'' +
            ", value='" + value + '\'' +
            '}';
    }
}
