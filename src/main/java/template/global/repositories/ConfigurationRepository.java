package template.global.repositories;

import template.global.entities.Configuration;

import java.util.Optional;

public interface ConfigurationRepository {
    Optional<Configuration> getByKey(String key);

    void save(Configuration authenticationConfiguration);
}
