package template.authorization.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.authorization.dtos.InternalRoleDto;
import template.authorization.entities.InternalRoleEntity;
import template.authorization.models.InternalRole;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Mapper(componentModel = "spring")
public interface InternalRoleMapper {
    List<InternalRole> dtoToList(List<InternalRoleDto> roleDtoList);

    InternalRole dtoToObject(InternalRoleDto roleDto);

    List<InternalRole> entityToList(
        List<InternalRoleEntity> roleEntityList);

    InternalRole entityToObject(InternalRoleEntity roleEntity);

    InternalRoleDto toDto(InternalRole role);

    List<InternalRoleDto> toDtoList(List<InternalRole> roleList);

    InternalRoleEntity toEntity(InternalRole role);

    List<InternalRoleEntity> toEntityList(List<InternalRole> roleList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget InternalRoleEntity roleEntity,
                InternalRole role);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }
}