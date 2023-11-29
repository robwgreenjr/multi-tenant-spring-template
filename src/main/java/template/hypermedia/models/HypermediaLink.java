package template.hypermedia.models;

import java.util.Objects;

public class HypermediaLink {
    private String href;
    private String type;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HypermediaLink that = (HypermediaLink) o;
        return Objects.equals(href, that.href) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(href, type);
    }
}
