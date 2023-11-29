package template.global.services;


import template.global.models.ConfigurationModel;

public interface ConfigurationManager {
    ConfigurationModel findByKey(String key);

    ConfigurationModel update(
        ConfigurationModel authenticationConfigurationModel);

    ConfigurationModel update(String key, String value, boolean hash);
}
