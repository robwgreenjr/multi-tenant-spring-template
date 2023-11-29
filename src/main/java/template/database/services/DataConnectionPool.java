package template.database.services;

import com.zaxxer.hikari.HikariDataSource;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import template.global.constants.GlobalVariable;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.AbstractDataSource;

import java.sql.Connection;
import java.sql.SQLException;

@Configuration
public class DataConnectionPool extends AbstractDataSource {
    private final Environment environment;

    public DataConnectionPool(DatabaseConnection databaseConnection,
                              Environment environment) {
        this.environment = environment;

        HikariDataSource dataSource = databaseConnection.getDefault();

        ApplicationDataSource mainConnection = new ApplicationDataSource();
        mainConnection.setDataSource(dataSource);

        DatabaseConnectionContext.getTargetDataSources()
            .put(mainConnection.getDataSource().getJdbcUrl(), mainConnection);
    }

    @Override
    public Connection getConnection() throws SQLException {
        /*
         *  Use default main connection if nothing has been set
         */
        if (DatabaseConnectionContext.getCurrentDatabaseConnection() == null) {
            return DatabaseConnectionContext.getTargetDataSources()
                .get(environment.getProperty(GlobalVariable.DATABASE_URL))
                .getDataConnection();
        }

        /*
         *  Use existing connection if exists
         */
        if (DatabaseConnectionContext.getTargetDataSources()
            .get(DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getJdbcUrl()) != null) {
            return DatabaseConnectionContext.getTargetDataSources()
                .get(DatabaseConnectionContext.getCurrentDatabaseConnection()
                    .getDataSource()
                    .getJdbcUrl())
                .getDataConnection();
        }

        /*
         *  Create new connection and add it to context
         */
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(
            environment.getProperty(GlobalVariable.DATABASE_DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(
            DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getJdbcUrl());
        dataSource.setUsername(
            DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getUsername());
        dataSource.setPassword(
            DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getPassword());
        dataSource.setMinimumIdle(
            DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getMinimumIdle());
        dataSource.setMaximumPoolSize(
            DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getMaximumPoolSize());

        ApplicationDataSource alternateConnection = new ApplicationDataSource();
        alternateConnection.setDataSource(dataSource);

        DatabaseConnectionContext.getTargetDataSources()
            .put(DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getJdbcUrl(), alternateConnection);

        return DatabaseConnectionContext.getTargetDataSources()
            .get(DatabaseConnectionContext.getCurrentDatabaseConnection().getDataSource()
                .getJdbcUrl())
            .getDataConnection();
    }

    @Override
    public Connection getConnection(String username, String password) {
        return null;
    }
}
