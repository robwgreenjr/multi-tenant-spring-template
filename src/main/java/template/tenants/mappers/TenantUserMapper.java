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
    List<TenantUser> dtoToList(
        List<TenantUserDto> tenantDtoList);

    TenantUser dtoToObject(TenantUserDto tenantDto);

    List<TenantUser> entityToList(
        List<TenantUserEntity> tenantEntityList);

    TenantUser entityToObject(
        TenantUserEntity tenantEntity);

    TenantUserDto toDto(TenantUser tenant);

    List<TenantUserDto> toDtoList(
        List<TenantUser> tenantList);

    TenantUserEntity toEntity(TenantUser tenant);

    List<TenantUserEntity> toEntityList(
        List<TenantUser> tenantList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantUserEntity tenantEntity,
                TenantUser tenant);
}