package template.tenants.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.tenants.dtos.TenantUserDto;
import template.tenants.entities.TenantUserEntity;
import template.tenants.models.TenantUser;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantUserMapper {
    List<TenantUser> dtoToList(List<TenantUserDto> tenantUserDtoList);

    TenantUser dtoToObject(TenantUserDto tenantUserDto);

    List<TenantUser> entityToList(List<TenantUserEntity> tenantUserEntityList);

    TenantUser entityToObject(TenantUserEntity tenantUserEntity);

    TenantUserDto toDto(TenantUser tenantUser);

    List<TenantUserDto> toDtoList(List<TenantUser> tenantUserList);

    TenantUserEntity toEntity(TenantUser tenantUser);

    List<TenantUserEntity> toEntityList(List<TenantUser> tenantUserList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantUserEntity tenantUserEntity,
                TenantUser tenantUser);
}