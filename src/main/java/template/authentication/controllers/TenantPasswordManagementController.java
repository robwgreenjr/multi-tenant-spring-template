package template.authentication.controllers;

import org.springframework.web.bind.annotation.*;
import template.authentication.dtos.ChangePasswordDto;
import template.authentication.dtos.ForgotPasswordDto;
import template.authentication.dtos.ResetPasswordTokenDto;
import template.authentication.mappers.TenantUserPasswordMapper;
import template.authentication.models.TenantUserPassword;
import template.authentication.services.PasswordManagement;

@RestController
@RequestMapping("authentication/password")
public class TenantPasswordManagementController {
    private final PasswordManagement<TenantUserPassword> passwordManagement;
    private final TenantUserPasswordMapper userPasswordMapper;

    public TenantPasswordManagementController(
        PasswordManagement<TenantUserPassword> passwordManagement,
        TenantUserPasswordMapper userPasswordMapper) {
        this.passwordManagement = passwordManagement;
        this.userPasswordMapper = userPasswordMapper;
    }

    @PostMapping("forgot")
    public void forgot(@RequestBody ForgotPasswordDto forgotPasswordDto) {
        TenantUserPassword userPassword =
            userPasswordMapper.forgotPasswordDtoToObject(forgotPasswordDto);

        passwordManagement.forgot(userPassword);
    }

    @PostMapping("reset")
    public void reset(@RequestBody ResetPasswordTokenDto resetPasswordDto)
        throws Exception {
        TenantUserPassword userPassword =
            userPasswordMapper.resetPasswordDtoToObject(resetPasswordDto);

        passwordManagement.reset(userPassword);
    }

    @PutMapping
    public void change(@RequestBody ChangePasswordDto changePasswordDto)
        throws Exception {
        TenantUserPassword userPassword =
            userPasswordMapper.changePasswordDtoToObject(changePasswordDto);

        passwordManagement.change(userPassword);
    }
}