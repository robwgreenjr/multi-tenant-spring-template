package template.authentication.helpers;

import org.springframework.stereotype.Service;
import template.authentication.constants.AuthenticationVariable;
import template.authentication.models.InternalResetPasswordToken;
import template.aws.services.SESSender;
import template.global.exceptions.KnownServerException;
import template.global.models.ConfigurationModel;
import template.global.services.ConfigurationManager;
import template.global.utilities.TimeSpecialist;

@Service
public class SimpleAuthenticationEmailProvider
    implements AuthenticationEmailProvider {
    private final SESSender sesSender;
    private final TimeSpecialist timeSpecialist;
    private final ConfigurationManager authenticationConfigurationManager;

    public SimpleAuthenticationEmailProvider(SESSender sesSender,
                                             TimeSpecialist timeSpecialist,
                                             ConfigurationManager authenticationConfigurationManager) {
        this.sesSender = sesSender;
        this.timeSpecialist = timeSpecialist;
        this.authenticationConfigurationManager =
            authenticationConfigurationManager;
    }

    @Override
    public void sendForgotPasswordEmail(
        InternalResetPasswordToken resetPasswordTokenModel) {
        String[] to =
            new String[]{resetPasswordTokenModel.getUser().getEmail()};

        String frontendUrl = "";
        String createPasswordUrl =
            frontendUrl + resetPasswordTokenModel.getCreatePasswordUri();

        ConfigurationModel resetPasswordExpiration =
            authenticationConfigurationManager.findByKey(
                AuthenticationVariable.RESET_PASSWORD_EXPIRATION);

        if (resetPasswordExpiration == null) {
            throw new KnownServerException("Reset password token isn't set.");
        }

        String title =
            "<h2>Forgot your password " +
                resetPasswordTokenModel.getUser().getFirstName() +
                "?</h2>";
        String subject = "** Reset Forgotten Password **";

        String html = "<html><head></head><body>" + title +
            "<p>Click the link to reset your password: <a href='" +
            createPasswordUrl +
            "'>Reset Password</a></p><p>Your time window to reset your password is " +
            timeSpecialist.integerToHoursAndMinutes(
                Integer.parseInt(resetPasswordExpiration.getValue())) +
            ".</p></body></html>";
        String text =
            "Click the link to reset your password: " + createPasswordUrl +
                ". Your reset password token will expire in " +
                timeSpecialist.integerToHoursAndMinutes(
                    Integer.parseInt(
                        resetPasswordExpiration.getValue()));

        sesSender.sendEmail(to, subject, html, text);
    }

    @Override
    public void sendCreatePasswordEmail(
        InternalResetPasswordToken resetPasswordTokenModel) {
        String[] to =
            new String[]{resetPasswordTokenModel.getUser().getEmail()};

        String frontendUrl = "";
        String createPasswordUrl =
            frontendUrl + resetPasswordTokenModel.getCreatePasswordUri();

        ConfigurationModel createPasswordExpiration =
            authenticationConfigurationManager.findByKey(
                AuthenticationVariable.CREATE_PASSWORD_EXPIRATION);

        if (createPasswordExpiration == null) {
            throw new KnownServerException("Reset password token isn't set.");
        }

        String title =
            "<h2>Welcome " +
                resetPasswordTokenModel.getUser().getFirstName() +
                "!</h2>";
        String subject = "** Create New Password **";

        String html = "<html><head></head><body>" + title +
            "<p>Click the link to create your first password: <a href='" +
            createPasswordUrl +
            "'>Create Password</a></p><p>Your time window to create your password is " +
            timeSpecialist.integerToHoursAndMinutes(
                Integer.parseInt(createPasswordExpiration.getValue())) +
            ".</p></body></html>";
        String text =
            "Click the link to create your password: " + createPasswordUrl +
                ". Your create password token will expire in " +
                timeSpecialist.integerToHoursAndMinutes(
                    Integer.parseInt(
                        createPasswordExpiration.getValue()));

        sesSender.sendEmail(to, subject, html, text);
    }
}
