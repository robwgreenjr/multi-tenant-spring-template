package template.tenants.mappers;

import org.mapstruct.Mapper;
import template.tenants.entities.TenantEmailConfirmationEntity;
import template.tenants.models.TenantEmailConfirmation;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Mapper(componentModel = "spring")
public interface TenantEmailConfirmationMapper {
    TenantEmailConfirmation entityToObject(
        TenantEmailConfirmationEntity tenantEmailConfirmationEntity);

    TenantEmailConfirmationEntity toEntity(
        TenantEmailConfirmation tenantModelEmailConfirmation);

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
