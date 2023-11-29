package template.global.repositories;

import template.global.entities.Configuration;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigurationImpl implements ConfigurationRepository {
    private final IConfigurationRepository configurationRepository;


    public ConfigurationImpl(
        IConfigurationRepository configurationRepository) {
        this.configurationRepository = configurationRepository;
    }


    @Override
    public Optional<Configuration> getByKey(String key) {
        return configurationRepository.getByKey(key);
    }

    @Override
    public void save(Configuration configuration) {
        configurationRepository.save(configuration);
    }
}
