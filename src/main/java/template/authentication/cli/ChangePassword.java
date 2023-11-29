package template.authentication.cli;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import template.CliRunner;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.PasswordManagement;
import template.authentication.services.UserPasswordManager;

@ChangePasswordCommand
public class ChangePassword {
    private final ApplicationContext context;
    private final UserPasswordManager userPasswordManager;
    //    private final UserManager userManager;
    private final PasswordManagement passwordManagement;

    public ChangePassword(ApplicationContext context,
                          UserPasswordManager userPasswordManager,
//                          UserManager userManager,
                          PasswordManagement passwordManagement) {
        this.context = context;
        this.userPasswordManager = userPasswordManager;
//        this.userManager = userManager;
        this.passwordManagement = passwordManagement;
    }

    public void run(ApplicationArguments args) {
        if (!args.containsOption("email")) {
            System.out.println(
                "An email argument must be provided, use --email=your-email");

            CliRunner.shutdown(context);
            return;
        }

        if (!args.containsOption("password")) {
            System.out.println(
                "An password argument must be provided, use --password=your-password");

            CliRunner.shutdown(context);
            return;
        }

        String email = args.getOptionValues("email").get(0);
        String password = args.getOptionValues("password").get(0);

        InternalUserPassword userPasswordModel;
        try {
            userPasswordModel = userPasswordManager.findByUserEmail(
                args.getOptionValues("email").get(0));
        } catch (Exception exception) {
//            UserModel userModel;
//            try {
//                userModel = userManager.getByEmail(email);
//            } catch (Exception e) {
//                System.out.println(
//                    "User wasn't found with provided email: " + email);
//
//                CliRunner.shutdown(context);
//                return;
//            }

            userPasswordModel = new InternalUserPassword();
//            userPasswordModel.setUser(userModel);
        }

        userPasswordModel.setPassword(password);

        try {
            passwordManagement.changeFORCE(userPasswordModel);
        } catch (Exception exception) {
            System.out.println("Error updating your password.");
        }

        CliRunner.shutdown(context);
    }
}
