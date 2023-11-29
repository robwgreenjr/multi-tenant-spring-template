package template.tenants.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import template.database.services.DatabaseConnection;
import template.database.services.LiquibaseTenantMigration;
import template.global.services.StringEncoder;
import template.tenants.exceptions.InvalidTenantEmailException;
import template.tenants.models.Tenant;
import template.tenants.models.TenantEmailConfirmation;
import template.tenants.repositories.TenantDatabaseRepository;

import java.sql.SQLException;

@Service
public class TenantRegistrationImpl implements TenantRegistration {
    private final TenantManager tenantManager;
    private final TenantEmailConfirmationManager tenantEmailConfirmationManager;
    private final TenantDatabaseManager tenantDatabaseManager;
    private final DatabaseConnection databaseConnection;
    private final LiquibaseTenantMigration liquibaseTenantMigration;
    private final StringEncoder cryptoEncoder;

    private final TenantDatabaseRepository tenantDatabaseRepository;


    public TenantRegistrationImpl(TenantManager tenantManager,
                                  TenantEmailConfirmationManager tenantEmailConfirmationManager,
                                  TenantDatabaseManager tenantDatabaseManager,
                                  DatabaseConnection databaseConnection,
                                  LiquibaseTenantMigration liquibaseTenantMigration,
                                  @Qualifier("CryptoEncoder")
                                  StringEncoder cryptoEncoder,
                                  TenantDatabaseRepository tenantDatabaseRepository) {
        this.tenantManager = tenantManager;
        this.tenantEmailConfirmationManager = tenantEmailConfirmationManager;
        this.tenantDatabaseManager = tenantDatabaseManager;
        this.databaseConnection = databaseConnection;
        this.liquibaseTenantMigration = liquibaseTenantMigration;
        this.cryptoEncoder = cryptoEncoder;
        this.tenantDatabaseRepository = tenantDatabaseRepository;
    }

    @Override
    public void register(Tenant tenantModel) {
        if (!tenantModel.checkIfValidEmail()) {
            throw new InvalidTenantEmailException();
        }

//        if (tenantDatabaseRepository.count() >= 1) {
//            throw new TenantRegistrationClosedException();
//        }

        tenantModel.setSubdomainFromEmail();

        Tenant createdTenant = tenantManager.create(tenantModel);
        TenantEmailConfirmation tenantEmailConfirmationModel =
            new TenantEmailConfirmation();
        tenantEmailConfirmationModel.setTenant(createdTenant);

        tenantEmailConfirmationManager.create(tenantEmailConfirmationModel);
    }

    @Override
    public void buildNewTenant(String confirmationToken) throws SQLException {
//        TenantEmailConfirmation tenantEmailConfirmationModel =
//            tenantEmailConfirmationManager.getByToken(confirmationToken);
//
//        TenantDatabase tenantDatabase =
//            tenantDatabaseManager.create(
//                tenantEmailConfirmationModel.getTenant());
//
//        HikariDataSource dataSource = databaseConnection.getDefault();
//        Connection mainConnection = dataSource.getConnection();
//
//        try {
//            mainConnection.prepareStatement("CREATE DATABASE \"" +
//                tenantDatabase.getTenant().getId() + "\";").execute();
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
//
//        String username = cryptoEncoder.decode(tenantDatabase.getUsername());
//        try {
//            mainConnection.prepareStatement(
//                "CREATE USER \"" + username +
//                    "\" WITH ENCRYPTED PASSWORD '" +
//                    cryptoEncoder.decode(tenantDatabase.getPassword()) +
//                    "';").execute();
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
//
//        try {
//            mainConnection.prepareStatement(
//                "GRANT ALL PRIVILEGES ON DATABASE \"" +
//                    tenantDatabase.getTenant().getId() + "\" TO \"" +
//                    username + "\";").execute();
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
//
//        dataSource = databaseConnection.getDatabase(tenantDatabase.getUrl());
//        Connection tenantConnection = dataSource.getConnection();
//        try {
//            tenantConnection.prepareStatement(
//                    "GRANT ALL ON ALL TABLES IN SCHEMA public TO \"" +
//                        username + "\";" +
//                        "GRANT USAGE, CREATE ON SCHEMA public TO \"" +
//                        username + "\";" +
//                        "GRANT ALL PRIVILEGES ON ALL FUNCTIONS IN SCHEMA public TO \"" +
//                        username + "\";" +
//                        "GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO \"" +
//                        username + "\";")
//                .execute();
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
//
//        try {
//            liquibaseTenantMigration.runMigration(tenantDatabase.getUrl(),
//                username,
//                cryptoEncoder.decode(tenantDatabase.getPassword()));
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
//
//        try {
//            tenantConnection.prepareStatement(
//                    "INSERT INTO user_base (email) VALUES ('" +
//                        tenantDatabase.getTenant().getEmail() +
//                        "');")
//                .execute();
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
//
//        try {
//            tenantConnection.prepareStatement(
//                    "INSERT INTO authorization_role_user (role_id, user_id) " +
//                        "VALUES ((SELECT id FROM authorization_role WHERE name = 'ADMIN')," +
//                        "        (SELECT id FROM user_base WHERE email = '" +
//                        tenantDatabase.getTenant().getEmail() + "'));")
//                .execute();
//        } catch (Exception ignored) {
//            // TODO: Better logging
//        }
    }
}
