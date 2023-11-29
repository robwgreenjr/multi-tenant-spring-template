package template.hypermedia.models;

import template.global.models.QueryResponse;

import java.util.List;

public class HypermediaResponse<T, S> {
    private T meta;
    private List<Object> errors;
    private S links;
    private List<Object> data;

    public HypermediaResponse(QueryResponse queryResponse, T meta, S links) {
        this.meta = meta;
        this.errors = queryResponse.getErrors();
        this.data = queryResponse.getData();
        this.links = links;
    }

    public T getMeta() {
        return meta;
    }

    public void setMeta(T meta) {
        this.meta = meta;
    }

    public S getLinks() {
        return links;
    }

    public void setLinks(S links) {
        this.links = links;
    }

    public List<Object> getErrors() {
        return errors;
    }

    public void setErrors(List<Object> errors) {
        this.errors = errors;
    }

    public List<Object> getData() {
        return data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }
}
