package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.authentication.models.TenantResetPasswordToken;
import template.authentication.models.TenantUserPassword;
import template.global.services.StringEncoder;

@Service("TenantPasswordManagement")
public class TenantPasswordManagement
    implements PasswordManagement<TenantUserPassword> {
    private final UserPasswordManager<TenantUserPassword> userPasswordManager;
    private final StringEncoder bCryptEncoder;
    private final ResetPasswordTokenManager<TenantResetPasswordToken>
        resetPasswordTokenManager;

    public TenantPasswordManagement(
        @Qualifier("TenantUserPasswordManager")
        UserPasswordManager<TenantUserPassword> userPasswordManager,
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        @Qualifier("TenantResetPasswordTokenManager")
        ResetPasswordTokenManager<TenantResetPasswordToken> resetPasswordTokenManager) {
        this.userPasswordManager = userPasswordManager;
        this.bCryptEncoder = bCryptEncoder;
        this.resetPasswordTokenManager = resetPasswordTokenManager;
    }

    @Override
    public void change(TenantUserPassword userPasswordModel)
        throws Exception {
        userPasswordModel.validatePassword();

//        TenantUserPassword foundUserPasswordModel =
//            userPasswordManager.findByUserEmail(
//                userPasswordModel.getEmailConfirmation());
//
//        if (!bCryptEncoder.verify(userPasswordModel.getCurrentPassword(),
//            foundUserPasswordModel.getPassword())) {
//            throw new PasswordIncorrectException();
//        }
//
//        setNewUserPassword(userPasswordModel, foundUserPasswordModel);

        updatePassword(userPasswordModel);
    }

    /**
     * Should only be used by CLI, not for end users
     */
    @Override
    public void changeFORCE(TenantUserPassword userPasswordModel)
        throws Exception {
        updatePassword(userPasswordModel);
    }

    @Override
    public void forgot(TenantUserPassword userPasswordModel) {
//        UserModel userModel = userManager.getByEmail(
//            userPasswordModel.getEmailConfirmation());

        TenantResetPasswordToken resetPasswordTokenModel =
            new TenantResetPasswordToken();
//        resetPasswordTokenModel.setUser(userModel);

        resetPasswordTokenManager.create(resetPasswordTokenModel);
    }

    @Override
    public void reset(TenantUserPassword userPasswordModel) throws Exception {
        userPasswordModel.validatePassword();

//        TenantResetPasswordToken resetPasswordTokenModel =
//            resetPasswordTokenManager.findByToken(
//                userPasswordModel.getToken());
//
//        TenantUserPassword foundUserPasswordModel;
//        try {
////            foundUserPasswordModel =
////                userPasswordManager.findByUserEmail(
////                    resetPasswordTokenModel.getUser().getEmail());
//        } catch (UserPasswordNotFoundException userPasswordNotFoundException) {
//            userPasswordModel.setUser(resetPasswordTokenModel.getUser());
//
//            foundUserPasswordModel =
//                userPasswordManager.create(userPasswordModel);
//        }

//        setNewUserPassword(userPasswordModel, foundUserPasswordModel);

        updatePassword(userPasswordModel);

        resetPasswordTokenManager.delete(userPasswordModel.getToken());
    }

    private void updatePassword(TenantUserPassword userPasswordModel)
        throws Exception {
        userPasswordModel.setPreviousPassword(
            userPasswordModel.getCurrentPassword());
        userPasswordModel.setPassword(
            bCryptEncoder.encode(userPasswordModel.getPassword()));

        userPasswordManager.update(userPasswordModel.getId(),
            userPasswordModel);
    }

    private void setNewUserPassword(TenantUserPassword userPasswordModel,
                                    TenantUserPassword foundUserPasswordModel) {
        userPasswordModel.setId(foundUserPasswordModel.getId());
        userPasswordModel.setUser(foundUserPasswordModel.getUser());
        userPasswordModel.setCurrentPassword(
            foundUserPasswordModel.getPassword());
        userPasswordModel.setCreatedOn(foundUserPasswordModel.getCreatedOn());
    }
}