package template.database.services;

import com.zaxxer.hikari.HikariDataSource;
import template.global.constants.GlobalVariable;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class DatabaseConnectionImpl implements DatabaseConnection {
    private final Environment environment;

    public DatabaseConnectionImpl(Environment environment) {
        this.environment = environment;
    }

    @Override
    public HikariDataSource getDefault() {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(
            environment.getProperty(
                GlobalVariable.DATABASE_DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(
            environment.getProperty(GlobalVariable.DATABASE_URL));
        dataSource.setUsername(
            environment.getProperty(GlobalVariable.DATABASE_USER));
        dataSource.setPassword(
            environment.getProperty(GlobalVariable.DATABASE_PASSWORD));
        dataSource.setMinimumIdle(2);
        dataSource.setMaximumPoolSize(5);

        return dataSource;
    }

    @Override
    public HikariDataSource getDatabase(String url, String username,
                                        String password) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(
            environment.getProperty(
                GlobalVariable.DATABASE_DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(2);

        return dataSource;
    }

    @Override
    public HikariDataSource getDatabase(String url) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(
            environment.getProperty(
                GlobalVariable.DATABASE_DRIVER_CLASS_NAME));
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(
            environment.getProperty(GlobalVariable.DATABASE_USER));
        dataSource.setPassword(
            environment.getProperty(GlobalVariable.DATABASE_PASSWORD));
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(2);

        return dataSource;
    }
}
