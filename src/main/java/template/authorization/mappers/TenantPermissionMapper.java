package template.authorization.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.authorization.dtos.TenantPermissionDto;
import template.authorization.entities.TenantPermissionEntity;
import template.authorization.models.TenantPermission;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantPermissionMapper {
    List<TenantPermission> dtoToList(
        List<TenantPermissionDto> permissionDtoList);

    TenantPermission dtoToObject(TenantPermissionDto permissionDto);

    List<TenantPermission> entityToList(
        List<TenantPermissionEntity> permissionEntityList);

    TenantPermission entityToObject(
        TenantPermissionEntity permissionEntity);

    TenantPermissionDto toDto(TenantPermission permission);

    List<TenantPermissionDto> toDtoList(List<TenantPermission> permissionList);

    TenantPermissionEntity toEntity(TenantPermission permission);

    List<TenantPermissionEntity> toEntityList(
        List<TenantPermission> permissionList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantPermissionEntity permissionEntity,
                TenantPermission permission);
}