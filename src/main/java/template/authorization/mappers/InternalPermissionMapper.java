package template.authorization.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.authorization.dtos.InternalPermissionDto;
import template.authorization.entities.InternalPermissionEntity;
import template.authorization.models.InternalPermission;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InternalPermissionMapper {
    List<InternalPermission> dtoToList(
        List<InternalPermissionDto> permissionDtoList);

    InternalPermission dtoToObject(InternalPermissionDto permissionDto);

    List<InternalPermission> entityToList(
        List<InternalPermissionEntity> permissionEntityList);

    InternalPermission entityToObject(
        InternalPermissionEntity permissionEntity);

    InternalPermissionDto toDto(InternalPermission permission);

    List<InternalPermissionDto> toDtoList(
        List<InternalPermission> permissionList);

    InternalPermissionEntity toEntity(InternalPermission permission);

    List<InternalPermissionEntity> toEntityList(
        List<InternalPermission> permissionList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget InternalPermissionEntity permissionEntity,
                InternalPermission permission);
}