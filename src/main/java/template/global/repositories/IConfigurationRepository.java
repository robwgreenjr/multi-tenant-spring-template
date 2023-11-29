package template.global.repositories;


import template.global.entities.Configuration;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IConfigurationRepository
    extends JpaRepository<Configuration, String> {

    Optional<Configuration> getByKey(String key);
}
