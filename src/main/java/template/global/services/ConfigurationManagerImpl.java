package template.global.services;

import template.global.entities.Configuration;
import template.global.exceptions.ConfigurationNotFoundException;
import template.global.mappers.ConfigurationMapper;
import template.global.models.ConfigurationModel;
import template.global.repositories.ConfigurationRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ConfigurationManagerImpl implements ConfigurationManager {
    private final ConfigurationRepository configurationRepository;
    private final ConfigurationMapper configurationMapper;

    public ConfigurationManagerImpl(
        ConfigurationRepository configurationRepository,
        ConfigurationMapper configurationMapper) {
        this.configurationRepository = configurationRepository;
        this.configurationMapper = configurationMapper;
    }

    @Override
    public ConfigurationModel findByKey(String key) {
        Optional<Configuration> configuration =
            configurationRepository.getByKey(key);

        if (configuration.isEmpty()) {
            throw new ConfigurationNotFoundException();
        }

        return configurationMapper.toConfigurationModel(
            configuration.get());
    }

    @Override
    public ConfigurationModel update(
        ConfigurationModel configurationModel) {
        Configuration entity =
            configurationMapper.configurationModelToConfiguration(
                configurationModel);

        configurationRepository.save(entity);

        return configurationModel;
    }

    @Override
    public ConfigurationModel update(String key, String value, boolean hash) {
        ConfigurationModel configurationModel = new ConfigurationModel();
        configurationModel.setKey(key);
        configurationModel.setValue(value);
        configurationModel.setHashed(hash);

        Configuration entity =
            configurationMapper.configurationModelToConfiguration(
                configurationModel);

        configurationRepository.save(entity);

        return configurationModel;
    }
}
