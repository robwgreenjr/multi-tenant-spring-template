package template.global.services;

import java.util.List;

public interface ListManager<T> {
    List<T> createAll(List<T> list);

    List<T> updateAll(List<T> list);
}
