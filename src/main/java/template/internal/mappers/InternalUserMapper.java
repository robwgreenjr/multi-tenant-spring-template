package template.internal.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.internal.dtos.InternalUserDto;
import template.internal.entities.InternalUserEntity;
import template.internal.models.InternalUser;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InternalUserMapper {
    List<InternalUser> dtoToList(
        List<InternalUserDto> permissionDtoList);

    InternalUser dtoToObject(InternalUserDto permissionDto);

    List<InternalUser> entityToList(
        List<InternalUserEntity> permissionEntityList);

    InternalUser entityToObject(
        InternalUserEntity permissionEntity);

    InternalUserDto toDto(InternalUser permission);

    List<InternalUserDto> toDtoList(
        List<InternalUser> permissionList);

    InternalUserEntity toEntity(InternalUser permission);

    List<InternalUserEntity> toEntityList(
        List<InternalUser> permissionList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget InternalUserEntity permissionEntity,
                InternalUser permission);
}