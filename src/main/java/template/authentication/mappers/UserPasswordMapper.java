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
public interface UserPasswordMapper {
    InternalUserPassword toUserPasswordModel(
        InternalUserPasswordEntity userPassword);

    InternalUserPasswordEntity userPasswordModelToUserPassword(
        InternalUserPassword userPasswordModel);

    InternalUserPassword changePasswordDtoToUserPasswordModel(
        ChangePasswordDto changePasswordDto);

    @Mapping(source = "email", target = "emailConfirmation")
    InternalUserPassword forgotPasswordDtoToUserPasswordModel(
        ForgotPasswordDto forgotPasswordDto);

    InternalUserPassword resetPasswordDtoToUserPasswordModel(
        ResetPasswordTokenDto resetPasswordDto);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget InternalUserPasswordEntity userPassword,
                InternalUserPassword userPasswordModel);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }
}