package template.global.mappers;

import template.global.dtos.ConfigurationDto;
import template.global.entities.Configuration;
import template.global.models.ConfigurationModel;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(componentModel = "spring")
public interface ConfigurationMapper {
    ConfigurationDto configurationModelToConfigurationDto(
        ConfigurationModel configurationModel);

    ConfigurationModel configurationDtoToConfigurationModel(
        ConfigurationDto configurationDto);

    ConfigurationModel toConfigurationModel(
        Configuration configuration);

    Configuration configurationModelToConfiguration(
        ConfigurationModel configurationModel);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget Configuration configuration,
                ConfigurationModel configurationModel);
}
