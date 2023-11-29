package template.global.models;

import jakarta.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class QueryResponse<T> {
    private T meta;
    private List<Object> errors;
    private List<Object> data;

    public QueryResponse(Object object, T meta) {
        this.meta = meta;
        this.data = new ArrayList<>();

        this.errors = new ArrayList<>();
        this.errors.add(object);
    }

    public QueryResponse(Object object, HttpServletResponse response, T meta) {
        buildDataAndError(object, response);

        this.meta = meta;
    }

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryResponse<?> that = (QueryResponse<?>) o;
        return Objects.equals(meta, that.meta) &&
            Objects.equals(errors, that.errors) &&
            Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(meta, errors, data);
    }

    private void buildDataAndError(Object object,
                                   HttpServletResponse response) {
        if (object == null) {
            this.data = new ArrayList<>();
        } else if (Collection.class.isAssignableFrom(object.getClass())) {
            this.data = (List<Object>) object;
        } else {
            this.data = new ArrayList<>();
            this.data.add(object);
        }

        if (this.errors == null) {
            this.errors = new ArrayList<>();
        }

        if (response.getStatus() >= 400) {
            if (!this.data.isEmpty()) {
                this.errors.add(this.data.get(0));
            }

            this.data = new ArrayList<>();
        }
    }
}
