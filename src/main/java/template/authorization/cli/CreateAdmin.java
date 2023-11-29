package template.authorization.cli;

import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import template.CliRunner;
import template.authorization.models.InternalRole;
import template.authorization.services.InternalRoleManager;

@CreateAdminCommand
public class CreateAdmin {
    private final ApplicationContext context;
    //    private final UserManager userManager;
    private final InternalRoleManager roleManager;

    public CreateAdmin(ApplicationContext context,
                       InternalRoleManager roleManager) {
        this.context = context;
        this.roleManager = roleManager;
    }

    public void run(ApplicationArguments args) {
        if (!args.containsOption("email")) {
            System.out.println(
                "An email argument must be provided, use --email=your-email");

            CliRunner.shutdown(context);
            return;
        }

//        UserModel userModel;
        String email = args.getOptionValues("email").get(0);
        try {
//            userModel = userManager.getByEmail(email);
        } catch (Exception exception) {
            System.out.println(
                "User wasn't found with provided email: " + email);

            CliRunner.shutdown(context);
            return;
        }

        InternalRole roleModel;
        try {
            roleModel = roleManager.getByName("ADMIN");
        } catch (Exception exception) {
            System.out.println(
                "For some reason the admin role hasn't been added to the database.");

            CliRunner.shutdown(context);
            return;
        }
//        Set<UserModel> users = roleModel.getUsers();
//        users.add(userModel);
//        roleModel.setUsers(users);
//        roleManager.update(roleModel.getId(), roleModel);

        CliRunner.shutdown(context);
    }
}
