package template.authentication.controllers;

import org.springframework.web.bind.annotation.*;
import template.authentication.dtos.ChangePasswordDto;
import template.authentication.dtos.ForgotPasswordDto;
import template.authentication.dtos.ResetPasswordTokenDto;
import template.authentication.mappers.UserPasswordMapper;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.PasswordManagement;

@RestController
@RequestMapping("authentication/password")
public class PasswordManagementController {
    private final PasswordManagement passwordManagement;
    private final UserPasswordMapper userPasswordMapper;

    public PasswordManagementController(PasswordManagement passwordManagement,
                                        UserPasswordMapper userPasswordMapper) {
        this.passwordManagement = passwordManagement;
        this.userPasswordMapper = userPasswordMapper;
    }

    @PostMapping("forgot")
    public void forgot(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        InternalUserPassword userPasswordModel =
            userPasswordMapper.forgotPasswordDtoToUserPasswordModel(
                forgotPasswordDto);

        passwordManagement.forgot(userPasswordModel);
    }

    @PostMapping("reset")
    public void reset(@RequestBody ResetPasswordTokenDto resetPasswordDto)
        throws Exception {
        InternalUserPassword userPasswordModel =
            userPasswordMapper.resetPasswordDtoToUserPasswordModel(
                resetPasswordDto);

        passwordManagement.reset(userPasswordModel);
    }

    @PutMapping
    public void change(@RequestBody ChangePasswordDto changePasswordDto)
        throws Exception {
        InternalUserPassword userPasswordModel =
            userPasswordMapper.changePasswordDtoToUserPasswordModel(
                changePasswordDto);

        passwordManagement.change(userPasswordModel);
    }
}