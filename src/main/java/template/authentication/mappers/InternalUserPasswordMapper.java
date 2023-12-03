package template.authentication.mappers;

import org.mapstruct.*;
import template.authentication.dtos.ChangePasswordDto;
import template.authentication.dtos.ForgotPasswordDto;
import template.authentication.dtos.ResetPasswordTokenDto;
import template.authentication.entities.InternalUserPasswordEntity;
import template.authentication.models.InternalUserPassword;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface InternalUserPasswordMapper {
    InternalUserPassword changePasswordDtoToObject(
        ChangePasswordDto changePasswordDto);

    @Mapping(target = "user.", source = "user")
    InternalUserPassword entityToObject(
        InternalUserPasswordEntity userPasswordEntity);

    @Mapping(source = "email", target = "emailConfirmation")
    InternalUserPassword forgotPasswordDtoToObject(
        ForgotPasswordDto forgotPasswordDto);

    InternalUserPassword resetPasswordDtoToObject(
        ResetPasswordTokenDto resetPasswordDto);

    @Mapping(target = "user.", source = "user")
    InternalUserPasswordEntity toEntity(InternalUserPassword userPassword);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget InternalUserPasswordEntity userPasswordEntity,
                InternalUserPassword userPassword);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }
}