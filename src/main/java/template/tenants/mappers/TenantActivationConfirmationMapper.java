package template.tenants.mappers;

import org.mapstruct.Mapper;
import template.tenants.dtos.TenantActivationConfirmationDto;
import template.tenants.models.TenantActivationConfirmation;
import template.tenants.models.TenantUser;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface TenantActivationConfirmationMapper {
    TenantActivationConfirmationDto toDto(
        TenantActivationConfirmation tenantActivationConfirmation);

    TenantActivationConfirmation dtoToObject(
        TenantActivationConfirmationDto tenantActivationConfirmationDto);

    TenantUser toTenantUser(
        TenantActivationConfirmation tenantActivationConfirmation);

    default UUID map(String string) {
        return UUID.fromString(string);
    }

    default String map(UUID uuid) {
        return uuid.toString();
    }
}
