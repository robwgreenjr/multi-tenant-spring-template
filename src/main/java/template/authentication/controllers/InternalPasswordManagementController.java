package template.authentication.controllers;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import template.authentication.dtos.ChangePasswordDto;
import template.authentication.dtos.ForgotPasswordDto;
import template.authentication.dtos.ResetPasswordTokenDto;
import template.authentication.mappers.InternalUserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.PasswordManagement;

@RestController
@RequestMapping("internal/authentication/password")
public class InternalPasswordManagementController {
    private final PasswordManagement<InternalUserPassword> passwordManagement;
    private final InternalUserPasswordMapper userPasswordMapper;

    public InternalPasswordManagementController(
        @Qualifier("InternalPasswordManagement")
        PasswordManagement<InternalUserPassword> passwordManagement,
        InternalUserPasswordMapper userPasswordMapper) {
        this.passwordManagement = passwordManagement;
        this.userPasswordMapper = userPasswordMapper;
    }

    @PostMapping("forgot")
    public void forgot(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        InternalUserPassword userPasswordModel =
            userPasswordMapper.forgotPasswordDtoToObject(forgotPasswordDto);

        passwordManagement.forgot(userPasswordModel);
    }

    @PostMapping("reset")
    public void reset(@RequestBody ResetPasswordTokenDto resetPasswordDto)
        throws Exception {
        InternalUserPassword userPasswordModel =
            userPasswordMapper.resetPasswordDtoToObject(resetPasswordDto);

        passwordManagement.reset(userPasswordModel);
    }

    @PutMapping
    public void change(@RequestBody ChangePasswordDto changePasswordDto)
        throws Exception {
        InternalUserPassword userPasswordModel =
            userPasswordMapper.changePasswordDtoToObject(changePasswordDto);

        passwordManagement.change(userPasswordModel);
    }
}