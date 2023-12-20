package template.tenants.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import template.tenants.dtos.TenantDto;
import template.tenants.entities.TenantEntity;
import template.tenants.models.Tenant;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TenantMapper {
    List<Tenant> entityToList(List<TenantEntity> tenantEntityList);

    Tenant entityToObject(TenantEntity tenantEntity);

    TenantEntity toEntity(Tenant tenant);

    TenantDto toDto(Tenant tenant);

    Tenant dtoToObject(TenantDto tenantDto);

    List<TenantDto> toDtoList(List<Tenant> tenantList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantEntity tenantEntity, Tenant tenant);

    default LocalDateTime map(String string) {
        if (string == null) return null;

        DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return LocalDateTime.parse(string, formatter);
    }

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }

    default UUID mapUUID(String string) {
        if (string == null) {
            return null;
        }

        return UUID.fromString(string);
    }

    default String map(UUID uuid) {
        if (uuid == null) {
            return null;
        }

        return uuid.toString();
    }
}