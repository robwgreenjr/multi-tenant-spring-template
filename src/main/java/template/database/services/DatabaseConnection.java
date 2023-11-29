package template.database.services;

import com.zaxxer.hikari.HikariDataSource;

public interface DatabaseConnection {
    HikariDataSource getDefault();

    HikariDataSource getDatabase(String url, String username, String password);

    HikariDataSource getDatabase(String url);
}
