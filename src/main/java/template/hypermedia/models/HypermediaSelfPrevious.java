package template.hypermedia.models;

import java.util.Objects;

public class HypermediaSelfPrevious extends HypermediaSelf {
    private HypermediaLink previous;


    public HypermediaLink getPrevious() {
        return previous;
    }

    public void setPrevious(HypermediaLink previous) {
        this.previous = previous;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HypermediaSelfPrevious that = (HypermediaSelfPrevious) o;
        return Objects.equals(previous, that.previous);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), previous);
    }
}
