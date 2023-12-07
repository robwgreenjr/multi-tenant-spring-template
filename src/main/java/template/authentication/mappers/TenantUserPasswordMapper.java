package template.authentication.mappers;

import org.mapstruct.*;
import template.authentication.dtos.ChangePasswordDto;
import template.authentication.dtos.ForgotPasswordDto;
import template.authentication.dtos.ResetPasswordTokenDto;
import template.authentication.entities.TenantUserPasswordEntity;
import template.authentication.models.TenantUserPassword;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Mapper(componentModel = "spring")
public interface TenantUserPasswordMapper {
    TenantUserPassword changePasswordDtoToObject(
        ChangePasswordDto changePasswordDto);

    TenantUserPassword entityToObject(
        TenantUserPasswordEntity userPasswordEntity);

    @Mapping(source = "email", target = "emailConfirmation")
    TenantUserPassword forgotPasswordDtoToObject(
        ForgotPasswordDto forgotPasswordDto);

    default LocalDateTime map(Instant instant) {
        return instant == null ? null : LocalDateTime.ofInstant(instant,
            ZoneOffset.UTC);
    }

    default Instant map(LocalDateTime localDateTime) {
        return localDateTime == null ? null :
            localDateTime.toInstant(ZoneOffset.UTC);
    }

    TenantUserPassword resetPasswordDtoToObject(
        ResetPasswordTokenDto resetPasswordDto);

    @Mapping(target = "user.", source = "user")
    TenantUserPasswordEntity toEntity(TenantUserPassword userPassword);

    @BeanMapping(
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void update(@MappingTarget TenantUserPasswordEntity userPasswordEntity,
                TenantUserPassword userPassword);
}