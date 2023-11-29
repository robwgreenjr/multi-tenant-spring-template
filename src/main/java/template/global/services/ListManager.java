package template.global.services;

import java.util.List;

public interface ListManager<T, ID> {
    List<T> createAll(List<T> modelList);

    List<T> updateAll(List<T> modelList);
}
