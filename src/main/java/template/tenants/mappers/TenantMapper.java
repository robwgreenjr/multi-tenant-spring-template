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

@Mapper(componentModel = "spring")
public interface TenantMapper {
    List<Tenant> toTenantModelList(List<TenantEntity> tenantList);

    Tenant toTenantModel(TenantEntity tenant);

    TenantEntity tenantModelToTenant(Tenant tenantModel);

    TenantDto tenantModelToTenantDto(Tenant tenantModel);

    Tenant tenantDtoToTenantModel(TenantDto tenantDto);

    List<TenantDto> tenantModelListToTenantDtoList(
        List<Tenant> tenantModelList);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantEntity tenant, Tenant tenantModel);

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
}