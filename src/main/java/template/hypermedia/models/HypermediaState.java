package template.hypermedia.models;

import java.util.List;
import java.util.Objects;

public class HypermediaState<T> {
    private T data;

    private List<HypermediaActionLink> actions;

    public HypermediaState(T data, List<HypermediaActionLink> actions) {
        this.data = data;
        this.actions = actions;
    }

    public List<HypermediaActionLink> getActions() {
        return actions;
    }

    public void setActions(List<HypermediaActionLink> actions) {
        this.actions = actions;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HypermediaState<?> that = (HypermediaState<?>) o;
        return Objects.equals(data, that.data) &&
            Objects.equals(actions, that.actions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data, actions);
    }
}
