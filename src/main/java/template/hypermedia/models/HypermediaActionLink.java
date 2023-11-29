package template.hypermedia.models;

import java.util.Objects;

public class HypermediaActionLink extends HypermediaLink {
    private String relation;

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        HypermediaActionLink that = (HypermediaActionLink) o;
        return Objects.equals(relation, that.relation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), relation);
    }
}
