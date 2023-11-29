package template.database.services;

import template.global.constants.GlobalVariable;
import liquibase.Contexts;
import liquibase.LabelExpression;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.net.UnknownServiceException;

@Service
public class LiquibaseTenantMigrationImpl implements LiquibaseTenantMigration {
    private final Environment environment;

    public LiquibaseTenantMigrationImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void runMigration(String url, String username, String password)
        throws UnknownServiceException {

        try {
            DataSource dataSource = DataSourceBuilder.create()
                .driverClassName(environment.getProperty(
                    GlobalVariable.DATABASE_DRIVER_CLASS_NAME))
                .url(url)
                .username(username)
                .password(password)
                .build();

            Database database = DatabaseFactory.getInstance()
                .findCorrectDatabaseImplementation(
                    new JdbcConnection(dataSource.getConnection()));

            Liquibase liquibase =
                new Liquibase("db/changelog/tenant/changelog.xml",
                    new ClassLoaderResourceAccessor(), database);
            liquibase.update(new Contexts(), new LabelExpression());
        } catch (Exception exception) {
            throw new UnknownServiceException(
                "Error creating your custom environment, please contact support.");
        }
    }
}
