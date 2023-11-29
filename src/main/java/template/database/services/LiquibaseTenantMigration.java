package template.database.services;

import liquibase.exception.LiquibaseException;

import java.net.UnknownServiceException;
import java.sql.SQLException;

public interface LiquibaseTenantMigration {
    void runMigration(String url, String username, String password)
        throws UnknownServiceException, SQLException, LiquibaseException;
}
