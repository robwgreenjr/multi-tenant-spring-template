package template.database.models;

import template.global.models.MetaData;

import java.util.Objects;

public class MetaQuery extends MetaData {
    private Integer count;
    private Integer page;
    private Integer pageCount;
    private Integer limit;
    private String cursor;
    private Integer next;
    private Integer previous;

    public MetaQuery() {
        this.limit = Query.MAX_LIMIT;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;

        this.pageCount = count / this.limit;
        // If we have leftovers we want to add that as the final page
        this.pageCount += count % this.limit > 0 ? 1 : 0;
    }

    public Integer getPageCount() {
        return pageCount;
    }

    public void setPageCount(Integer pageCount) {
        this.pageCount = pageCount;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public String getCursor() {
        return cursor;
    }

    public void setCursor(String cursor) {
        this.cursor = cursor;
    }

    public Integer getNext() {
        return next;
    }

    public void setNext(Integer next) {
        this.next = next;
    }

    public Integer getPrevious() {
        return previous;
    }

    public void setPrevious(Integer previous) {
        this.previous = previous;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MetaQuery metaQuery = (MetaQuery) o;
        return Objects.equals(count, metaQuery.count) &&
            Objects.equals(page, metaQuery.page) &&
            Objects.equals(pageCount, metaQuery.pageCount) &&
            Objects.equals(limit, metaQuery.limit) &&
            Objects.equals(cursor, metaQuery.cursor) &&
            Objects.equals(next, metaQuery.next) &&
            Objects.equals(previous, metaQuery.previous);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), count, page, pageCount, limit,
            cursor,
            next, previous);
    }
}
