package template.authentication.mappers;

import org.mapstruct.Mapper;
import template.authentication.entities.InternalResetPasswordTokenEntity;
import template.authentication.models.InternalResetPasswordToken;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface InternalResetPasswordTokenMapper {
    InternalResetPasswordToken entityToObject(
        InternalResetPasswordTokenEntity resetPasswordToken);

    InternalResetPasswordTokenEntity toEntity(
        InternalResetPasswordToken resetPasswordToken);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }
}