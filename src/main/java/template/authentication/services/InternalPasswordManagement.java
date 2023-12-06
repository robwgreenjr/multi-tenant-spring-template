package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.models.InternalUserPassword;
import template.global.services.StringEncoder;
import template.internal.exceptions.InternalUserNotFoundException;
import template.internal.models.InternalUser;
import template.internal.services.InternalUserManager;

import java.util.Optional;

@Service("InternalPasswordManagement")
public class InternalPasswordManagement
    implements PasswordManagement<InternalUserPassword> {
    private final UserPasswordManager<InternalUserPassword> userPasswordManager;
    private final StringEncoder bCryptEncoder;
    private final ResetPasswordTokenManager<InternalResetPasswordToken>
        resetPasswordTokenManager;
    private final InternalUserManager userManager;

    public InternalPasswordManagement(
        @Qualifier("InternalUserPasswordManager")
        UserPasswordManager<InternalUserPassword> userPasswordManager,
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        @Qualifier("InternalResetPasswordTokenManager")
        ResetPasswordTokenManager<InternalResetPasswordToken> resetPasswordTokenManager,
        InternalUserManager userManager) {
        this.userPasswordManager = userPasswordManager;
        this.bCryptEncoder = bCryptEncoder;
        this.resetPasswordTokenManager = resetPasswordTokenManager;
        this.userManager = userManager;
    }

    @Override
    public void change(InternalUserPassword userPassword)
        throws Exception {
        userPassword.validatePassword();

        Optional<InternalUserPassword> foundUserPassword =
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
    public void changeFORCE(InternalUserPassword userPassword)
        throws Exception {
        updatePassword(userPassword);
    }

    @Override
    public void forgot(InternalUserPassword userPassword) {
        Optional<InternalUser> foundUser =
            userManager.getByEmail(
                userPassword.getEmailConfirmation());

        if (foundUser.isEmpty()) {
            throw new InternalUserNotFoundException();
        }

        InternalResetPasswordToken resetPasswordToken =
            new InternalResetPasswordToken();
        resetPasswordToken.setUser(foundUser.get());

        resetPasswordTokenManager.create(resetPasswordToken);
    }

    @Override
    public void reset(InternalUserPassword userPassword) throws Exception {
        userPassword.validatePassword();

        Optional<InternalResetPasswordToken> resetPasswordToken =
            resetPasswordTokenManager.findByToken(userPassword.getToken());

        if (resetPasswordToken.isEmpty()) {
            throw new ResetPasswordTokenNotFoundException();
        }

        Optional<InternalUserPassword> foundUserPassword = Optional.empty();
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                "There was an error setting your password.");
        }

        setNewUserPassword(userPassword, foundUserPassword.get());

        updatePassword(userPassword);

        resetPasswordTokenManager.delete(userPassword.getToken());
    }

    private void updatePassword(InternalUserPassword userPassword)
        throws Exception {
        userPassword.setPreviousPassword(
            userPassword.getCurrentPassword());
        userPassword.setPassword(
            bCryptEncoder.encode(userPassword.getPassword()));

        userPasswordManager.update(userPassword.getId(),
            userPassword);
    }

    private void setNewUserPassword(InternalUserPassword userPassword,
                                    InternalUserPassword foundUserPassword) {
        userPassword.setId(foundUserPassword.getId());
        userPassword.setUser(foundUserPassword.getUser());
        userPassword.setCurrentPassword(
            foundUserPassword.getPassword());
        userPassword.setCreatedOn(foundUserPassword.getCreatedOn());
    }
}