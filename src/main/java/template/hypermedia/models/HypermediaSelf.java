package template.hypermedia.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HypermediaSelf {
    private HypermediaLink self;

    private List<HypermediaActionLink> actions = new ArrayList<>();

    public List<HypermediaActionLink> getActions() {
        return actions;
    }

    public void setActions(List<HypermediaActionLink> actions) {
        this.actions = actions;
    }

    public HypermediaLink getSelf() {
        return self;
    }

    public void setSelf(HypermediaLink self) {
        this.self = self;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HypermediaSelf that = (HypermediaSelf) o;
        return Objects.equals(self, that.self) &&
            Objects.equals(actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(self, actions);
    }
}
