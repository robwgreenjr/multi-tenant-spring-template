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
