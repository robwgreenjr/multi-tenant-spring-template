package template.authentication.mappers;

import org.mapstruct.Mapper;
import template.authentication.entities.TenantResetPasswordTokenEntity;
import template.authentication.models.TenantResetPasswordToken;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface TenantResetPasswordTokenMapper {
    TenantResetPasswordToken entityToObject(
        TenantResetPasswordTokenEntity resetPasswordToken);

    TenantResetPasswordTokenEntity toEntity(
        TenantResetPasswordToken resetPasswordToken);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }
}