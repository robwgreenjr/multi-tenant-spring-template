package template.database.models;

import java.util.List;
import java.util.Objects;

public class QueryResult<T> {
    private List<T> data;
    private MetaQuery meta;

    public QueryResult() {
        this.meta = new MetaQuery();
    }

    public QueryResult(List<T> data) {
        this.meta = new MetaQuery();
        this.data = data;
    }

    public <D> QueryResult<D> mapData(List<D> data) {
        QueryResult<D> mappedData = new QueryResult<>();
        mappedData.setMeta(this.getMeta());
        mappedData.setData(data);

        return mappedData;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    public MetaQuery getMeta() {
        return meta;
    }

    public void setMeta(MetaQuery meta) {
        this.meta = meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResult<?> that = (QueryResult<?>) o;
        return Objects.equals(data, that.data) &&
            Objects.equals(meta, that.meta);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, meta);
    }
}
