package template.hypermedia.models;

import java.util.Objects;

public class HypermediaNext extends HypermediaSelf {
    private HypermediaLink next;


    public HypermediaLink getNext() {
        return next;
    }

    public void setNext(HypermediaLink next) {
        this.next = next;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HypermediaNext that = (HypermediaNext) o;
        return Objects.equals(next, that.next);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), next);
    }
}
