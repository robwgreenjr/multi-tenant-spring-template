package template.global.controllers;

import template.database.helpers.DatabaseExceptionHandler;
import template.global.dtos.ConfigurationDto;
import template.global.mappers.ConfigurationMapper;
import template.global.models.ConfigurationModel;
import template.global.services.ConfigurationManager;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("configuration")
public class ConfigurationController {
    private final ConfigurationManager configurationManager;
    private final ConfigurationMapper configurationMapper;
    private final DatabaseExceptionHandler databaseExceptionHandler;

    public ConfigurationController(
        ConfigurationManager configurationManager,
        ConfigurationMapper configurationMapper,
        DatabaseExceptionHandler databaseExceptionHandler) {
        this.configurationManager = configurationManager;
        this.configurationMapper = configurationMapper;
        this.databaseExceptionHandler = databaseExceptionHandler;
    }

    @GetMapping("{key}")
    public ConfigurationDto find(@PathVariable String key) throws Exception {
        ConfigurationModel configurationModel = new ConfigurationModel();

        try {
            configurationModel =
                configurationManager.findByKey(key);
        } catch (Exception exception) {
            databaseExceptionHandler.exceptionHandler(exception);
        }

        return configurationMapper.configurationModelToConfigurationDto(
            configurationModel);
    }

    @PatchMapping()
    public ConfigurationDto update(@RequestBody
                                   ConfigurationDto configurationDto) throws Exception {
        ConfigurationModel configurationModel =
            configurationMapper.configurationDtoToConfigurationModel(
                configurationDto);

        try {
            configurationModel =
                configurationManager.update(configurationModel);
        } catch (Exception exception) {
            databaseExceptionHandler.exceptionHandler(exception);
        }


        return configurationMapper.configurationModelToConfigurationDto(
            configurationModel);
    }
}
