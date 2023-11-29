package template;

import template.authentication.cli.ChangePassword;
import template.authorization.cli.CreateAdmin;
import template.database.cli.Seeder;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class CliRunner implements ApplicationRunner {

    private final Seeder seeder;
    private final CreateAdmin createAdmin;
    private final ChangePassword changePassword;

    public CliRunner(Seeder seeder, CreateAdmin createAdmin, ChangePassword changePassword) {
        this.seeder = seeder;
        this.createAdmin = createAdmin;
        this.changePassword = changePassword;
    }

    public static void shutdown(ApplicationContext context) {
        try {
            for (ApplicationDataSource connection : DatabaseConnectionContext.getTargetDataSources().values()) {
                connection.getDataSource().close();
            }
        } catch (Exception exception) {
            // do nothing
        }

        System.exit(SpringApplication.exit(context, () -> 0));
    }

    @Override
    public void run(ApplicationArguments args) {
        if (args.containsOption("seeder")) {
            seeder.run(args);
        }

        if (args.containsOption("create-admin")) {
            createAdmin.run(args);
        }

        if (args.containsOption("change-password")) {
            changePassword.run(args);
        }
    }
}
