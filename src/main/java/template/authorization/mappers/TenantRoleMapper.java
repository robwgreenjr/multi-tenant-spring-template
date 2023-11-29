package template.authorization.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.authorization.dtos.TenantRoleDto;
import template.authorization.entities.TenantRoleEntity;
import template.authorization.models.TenantRole;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TenantRoleMapper {
    List<TenantRole> dtoToList(List<TenantRoleDto> roleDtoList);

    TenantRole dtoToObject(TenantRoleDto roleDto);

    List<TenantRole> entityToList(
        List<TenantRoleEntity> roleEntityList);

    TenantRole entityToObject(TenantRoleEntity roleEntity);

    TenantRoleDto toDto(TenantRole role);

    List<TenantRoleDto> toDtoList(List<TenantRole> roleList);

    TenantRoleEntity toEntity(TenantRole role);

    List<TenantRoleEntity> toEntityList(List<TenantRole> roleList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantRoleEntity role, TenantRole roleModel);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }
}