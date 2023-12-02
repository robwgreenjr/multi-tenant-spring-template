package template.authentication.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.models.InternalUserPassword;
import template.global.services.StringEncoder;

@Service("InternalPasswordManagement")
public class InternalPasswordManagement
    implements PasswordManagement<InternalUserPassword> {
    private final UserPasswordManager<InternalUserPassword> userPasswordManager;
    private final StringEncoder bCryptEncoder;
    private final ResetPasswordTokenManager<InternalResetPasswordToken>
        resetPasswordTokenManager;

    public InternalPasswordManagement(
        UserPasswordManager<InternalUserPassword> userPasswordManager,
        @Qualifier("BCryptEncoder")
        StringEncoder bCryptEncoder,
        ResetPasswordTokenManager<InternalResetPasswordToken> resetPasswordTokenManager) {
        this.userPasswordManager = userPasswordManager;
        this.bCryptEncoder = bCryptEncoder;
        this.resetPasswordTokenManager = resetPasswordTokenManager;
    }

    @Override
    public void change(InternalUserPassword userPasswordModel)
        throws Exception {
        userPasswordModel.validatePassword();

//        InternalUserPassword foundUserPasswordModel =
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
    public void changeFORCE(InternalUserPassword userPasswordModel)
        throws Exception {
        updatePassword(userPasswordModel);
    }

    @Override
    public void forgot(InternalUserPassword userPasswordModel) {
//        UserModel userModel = userManager.getByEmail(
//            userPasswordModel.getEmailConfirmation());

        InternalResetPasswordToken resetPasswordTokenModel =
            new InternalResetPasswordToken();
//        resetPasswordTokenModel.setUser(userModel);

        resetPasswordTokenManager.create(resetPasswordTokenModel);
    }

    @Override
    public void reset(InternalUserPassword userPasswordModel) throws Exception {
        userPasswordModel.validatePassword();

//        InternalResetPasswordToken resetPasswordTokenModel =
//            resetPasswordTokenManager.findByToken(
//                userPasswordModel.getToken());
//
//        InternalUserPassword foundUserPasswordModel;
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

    private void updatePassword(InternalUserPassword userPasswordModel)
        throws Exception {
        userPasswordModel.setPreviousPassword(
            userPasswordModel.getCurrentPassword());
        userPasswordModel.setPassword(
            bCryptEncoder.encode(userPasswordModel.getPassword()));

        userPasswordManager.update(userPasswordModel.getId(),
            userPasswordModel);
    }

    private void setNewUserPassword(InternalUserPassword userPasswordModel,
                                    InternalUserPassword foundUserPasswordModel) {
        userPasswordModel.setId(foundUserPasswordModel.getId());
        userPasswordModel.setUser(foundUserPasswordModel.getUser());
        userPasswordModel.setCurrentPassword(
            foundUserPasswordModel.getPassword());
        userPasswordModel.setCreatedOn(foundUserPasswordModel.getCreatedOn());
    }
}