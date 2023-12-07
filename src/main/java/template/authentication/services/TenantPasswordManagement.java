package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.models.TenantResetPasswordToken;
import template.authentication.models.TenantUserPassword;
import template.global.services.StringEncoder;
import template.tenants.exceptions.TenantUserNotFoundException;
import template.tenants.models.TenantUser;
import template.tenants.services.TenantUserManager;

import java.util.Optional;

@Service("TenantPasswordManagement")
public class TenantPasswordManagement
    implements PasswordManagement<TenantUserPassword> {
    private final UserPasswordManager<TenantUserPassword> userPasswordManager;
    private final StringEncoder bCryptEncoder;
    private final ResetPasswordTokenManager<TenantResetPasswordToken>
        resetPasswordTokenManager;
    private final TenantUserManager tenantUserManager;

    public TenantPasswordManagement(
        @Qualifier("TenantUserPasswordManager")
        UserPasswordManager<TenantUserPassword> userPasswordManager,
        TenantUserManager tenantUserManager,
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        @Qualifier("TenantResetPasswordTokenManager")
        ResetPasswordTokenManager<TenantResetPasswordToken> resetPasswordTokenManager) {
        this.userPasswordManager = userPasswordManager;
        this.bCryptEncoder = bCryptEncoder;
        this.resetPasswordTokenManager = resetPasswordTokenManager;
        this.tenantUserManager = tenantUserManager;
    }

    @Override
    public void change(TenantUserPassword userPassword) {
        userPassword.validatePassword();

        Optional<TenantUserPassword> foundUserPassword =
            userPasswordManager.findByUserEmail(
                userPassword.getEmailConfirmation());

        if (foundUserPassword.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        if (!bCryptEncoder.verify(userPassword.getCurrentPassword(),
            foundUserPassword.get().getPassword())) {
            throw new PasswordIncorrectException();
        }

        setNewUserPassword(userPassword, foundUserPassword.get());

        updatePassword(userPassword);
    }

    /**
     * Should only be used by CLI, not for end users
     */
    @Override
    public void changeFORCE(TenantUserPassword userPassword) {
        updatePassword(userPassword);
    }

    @Override
    public void forgot(TenantUserPassword userPassword) {
        Optional<TenantUser> user =
            tenantUserManager.findByEmail(userPassword.getEmailConfirmation());

        if (user.isEmpty()) {
            throw new TenantUserNotFoundException();
        }

        TenantResetPasswordToken resetPasswordToken =
            new TenantResetPasswordToken();
        resetPasswordToken.setUser(user.get());

        resetPasswordTokenManager.create(resetPasswordToken);
    }

    @Override
    public void reset(TenantUserPassword userPassword) throws Exception {
        userPassword.validatePassword();

        Optional<TenantResetPasswordToken> resetPasswordToken =
            resetPasswordTokenManager.findByToken(
                userPassword.getToken());

        if (resetPasswordToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        Optional<TenantUserPassword> foundUserPassword;
        try {
            foundUserPassword =
                userPasswordManager.findByUserEmail(
                    resetPasswordToken.get().getUser().getEmail());
        } catch (UserPasswordNotFoundException userPasswordNotFoundException) {
            userPassword.setUser(resetPasswordToken.get().getUser());

            foundUserPassword =
                Optional.of(userPasswordManager.create(userPassword));
        }

        if (foundUserPassword.isEmpty()) {
            throw new UserPasswordNotFoundException();
        }

        setNewUserPassword(userPassword, foundUserPassword.get());

        updatePassword(userPassword);

        resetPasswordTokenManager.delete(userPassword.getToken());
    }

    private void updatePassword(TenantUserPassword userPassword) {
        userPassword.setPreviousPassword(
            userPassword.getCurrentPassword());
        userPassword.setPassword(
            bCryptEncoder.encode(userPassword.getPassword()));

        userPasswordManager.update(userPassword.getId(),
            userPassword);
    }

    private void setNewUserPassword(TenantUserPassword userPassword,
                                    TenantUserPassword foundUserPassword) {
        userPassword.setId(foundUserPassword.getId());
        userPassword.setUser(foundUserPassword.getUser());
        userPassword.setCurrentPassword(
            foundUserPassword.getPassword());
        userPassword.setCreatedOn(foundUserPassword.getCreatedOn());
    }
}