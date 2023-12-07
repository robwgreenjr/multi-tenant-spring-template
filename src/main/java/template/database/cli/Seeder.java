package template.database.cli;

import com.zaxxer.hikari.HikariDataSource;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.ApplicationArguments;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import template.CliRunner;
import template.database.services.DatabaseConnection;
import template.global.services.StringEncoder;

import java.sql.Connection;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * This could potentially cause an infinite loop for failed inserts (prints failures)
 */
@SeederCommand
public class Seeder {
    private final ApplicationContext context;
    private final DatabaseConnection databaseConnection;
    private final StringEncoder stringEncoder;

    public Seeder(ApplicationContext context,
                  DatabaseConnection databaseConnection,
                  @Qualifier("BCryptEncoder")
                  StringEncoder stringEncoder) {
        this.context = context;
        this.databaseConnection = databaseConnection;
        this.stringEncoder = stringEncoder;
    }

    public void run(ApplicationArguments args) {
        if (!args.containsOption("schema")) {
            System.out.println(
                "A schema argument must be provided, use --schema=your-schema");

            CliRunner.shutdown(context);
            return;
        }

        if (!args.containsOption("table")) {
            System.out.println(
                "A table argument must be provided, use --table=your-table");

            CliRunner.shutdown(context);
            return;
        }

        if (!args.containsOption("count")) {
            System.out.println(
                "A count argument must be provided, use --count=your-count");

            CliRunner.shutdown(context);
            return;
        }

        String schema = args.getOptionValues("schema").get(0);
        String table = args.getOptionValues("table").get(0);
        String count = args.getOptionValues("count").get(0);

        // TODO: Check if table exists before attempting seeding
        HikariDataSource dataSource = databaseConnection.getDefault();
        JdbcTemplate jdbcTemplate;
        try {
            Connection connection = dataSource.getConnection();
            jdbcTemplate = new JdbcTemplate(
                new SingleConnectionDataSource(connection, true));
        } catch (Exception exception) {
            CliRunner.shutdown(context);
            return;
        }

        int successCount = 0;
        if (schema.equals("internal") && table.equals("user")) {
            successCount =
                internalUser(jdbcTemplate, Integer.valueOf(count));
        }

        System.out.println(
            "Successfully seeded " + successCount + " records into the table " +
                table + ".");
        CliRunner.shutdown(context);
    }

    public void defaultConfiguration(JdbcTemplate jdbcTemplate) {
        try {
            jdbcTemplate.update(
                "TRUNCATE TABLE internal.configuration CASCADE;");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        
        String insertQuery =
            "INSERT INTO internal.configuration (key, value) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try {
            jdbcTemplate.update(insertQuery, "JWT_SECRET", "this is a test");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        insertQuery =
            "INSERT INTO internal.configuration (key, value) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try {
            jdbcTemplate.update(insertQuery, "JWT_EXPIRATION",
                "2400");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        insertQuery =
            "INSERT INTO internal.configuration (key, value) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try {
            jdbcTemplate.update(insertQuery, "RESET_PASSWORD_EXPIRATION",
                "2400");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }

        insertQuery =
            "INSERT INTO internal.configuration (key, value) VALUES (?, ?) ON CONFLICT DO NOTHING";
        try {
            jdbcTemplate.update(insertQuery, "CREATE_PASSWORD_EXPIRATION",
                "2400");
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
    }

    public Integer internalUser(JdbcTemplate jdbcTemplate, Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO internal.user (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalUser(JdbcTemplate jdbcTemplate, Integer count,
                                String commonFirstName,
                                Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            if (numberOfCommonValues > successCount) {
                firstName += commonFirstName;
            } else {
                firstName = firstName.replace(commonFirstName, "");
            }

            String insertQuery =
                "INSERT INTO internal.user (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalUserResetPasswordToken(JdbcTemplate jdbcTemplate,
                                                  Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO internal.user (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone);

                String sql = "SELECT * FROM internal.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO internal.authentication_user_reset_password_token (user_id, token) VALUES (?, ?)";
                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    UUID.randomUUID());
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantUserResetPasswordToken(JdbcTemplate jdbcTemplate,
                                                UUID tenantId,
                                                Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO tenant.user (tenant_id, first_name, last_name, email, phone) VALUES (?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, tenantId, firstName, lastName,
                    email,
                    phone);

                String sql = "SELECT * FROM tenant.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO tenant.authentication_user_reset_password_token (tenant_id, user_id, token) VALUES (?, ?, ?)";
                jdbcTemplate.update(insertQuery, tenantId,
                    singleObject.get(0).get("id"),
                    UUID.randomUUID());
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalUserPassword(JdbcTemplate jdbcTemplate,
                                        Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO internal.user (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone);

                String sql = "SELECT * FROM internal.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO internal.authentication_user_password (user_id, password) VALUES (?, ?)";
                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    stringEncoder.encode("password"));
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantUserPassword(JdbcTemplate jdbcTemplate, UUID tenantId,
                                      Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO tenant.user (tenant_id, first_name, last_name, email, phone) VALUES (?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, tenantId, firstName, lastName,
                    email,
                    phone);

                String sql = "SELECT * FROM tenant.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO tenant.authentication_user_password (tenant_id, user_id, password) VALUES (?, ?, ?)";
                jdbcTemplate.update(insertQuery, tenantId,
                    singleObject.get(0).get("id"),
                    stringEncoder.encode("password"));
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalUserPasswordAndResetPasswordToken(
        JdbcTemplate jdbcTemplate,
        Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO internal.user (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone);

                String sql = "SELECT * FROM internal.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO internal.authentication_user_password (user_id, password) VALUES (?, ?)";
                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    stringEncoder.encode("password"));

                insertQuery =
                    "INSERT INTO internal.authentication_user_reset_password_token (user_id) VALUES (?)";
                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"));
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantUserPasswordAndResetPasswordToken(
        JdbcTemplate jdbcTemplate,
        UUID tenantId,
        Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            String insertQuery =
                "INSERT INTO tenant.user (tenant_id, first_name, last_name, email, phone) VALUES (?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, tenantId, firstName, lastName,
                    email,
                    phone);

                String sql = "SELECT * FROM tenant.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO tenant.authentication_user_password (tenant_id, user_id, password) VALUES (?, ?, ?)";
                jdbcTemplate.update(insertQuery, tenantId,
                    singleObject.get(0).get("id"),
                    stringEncoder.encode("password"));

                insertQuery =
                    "INSERT INTO tenant.authentication_user_reset_password_token (tenant_id, user_id) VALUES (?, ?)";
                jdbcTemplate.update(insertQuery, tenantId,
                    singleObject.get(0).get("id"));
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalUserResetPasswordToken(JdbcTemplate jdbcTemplate,
                                                  Integer count,
                                                  String commonFirstName,
                                                  Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            if (numberOfCommonValues > successCount) {
                firstName += commonFirstName;
            } else {
                firstName = firstName.replace(commonFirstName, "");
            }

            String insertQuery =
                "INSERT INTO internal.user (first_name, last_name, email, phone) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone);

                String sql = "SELECT * FROM internal.user WHERE email = ?";
                List<Map<String, Object>> singleObject =
                    jdbcTemplate.queryForList(sql, email);
                insertQuery =
                    "INSERT INTO internal.authentication_user_reset_password_token (user_id, token) VALUES (?, ?)";
                jdbcTemplate.update(insertQuery, singleObject.get(0).get("id"),
                    UUID.randomUUID());
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenant(JdbcTemplate jdbcTemplate,
                          Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String companyName = faker.company().name();
            String companyPhone = faker.phoneNumber().phoneNumber();
            String companyEmail = faker.internet().emailAddress();
            String companySubdomain = faker.internet().domainName();

            String insertQuery =
                "INSERT INTO internal.tenant (company_name, email, phone, subdomain) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, companyName, companyPhone,
                    companyEmail,
                    companySubdomain);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }

            successCount++;
        }

        return successCount;
    }

    public UUID createMainTenant(JdbcTemplate jdbcTemplate) {
        Faker faker = new Faker();

        String companyName = faker.company().name();
        String companyPhone = faker.phoneNumber().phoneNumber();
        String companyEmail = faker.internet().emailAddress();
        String companySubdomain = faker.internet().domainName();

        String insertQuery =
            "INSERT INTO internal.tenant (company_name, email, phone, subdomain) VALUES (?, ?, ?, ?) ON CONFLICT DO NOTHING";
        try {
            jdbcTemplate.update(insertQuery, companyName, companyPhone,
                companyEmail,
                companySubdomain);
        } catch (Exception exception) {
            companyName = faker.company().name();
            companyPhone = faker.phoneNumber().phoneNumber();
            companyEmail = faker.internet().emailAddress();
            companySubdomain = faker.internet().domainName();

            try {
                jdbcTemplate.update(insertQuery, companyName, companyPhone,
                    companyEmail,
                    companySubdomain);
            } catch (Exception exceptionTwo) {
                companyName = faker.company().name();
                companyPhone = faker.phoneNumber().phoneNumber();
                companyEmail = faker.internet().emailAddress();
                companySubdomain = faker.internet().domainName();

                try {
                    jdbcTemplate.update(insertQuery, companyName, companyPhone,
                        companyEmail,
                        companySubdomain);
                } catch (Exception exceptionThree) {
                    System.out.println(exception.getMessage());
                }
            }
        }

        String sql = "SELECT * FROM internal.tenant WHERE company_name = ?";
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(sql, companyName);

        return (UUID) singleObject.get(0).get("id");
    }


    public Integer tenant(JdbcTemplate jdbcTemplate,
                          Integer count, String commonCompanyName,
                          Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String companyName = faker.company().name();
            String companyPhone = faker.phoneNumber().phoneNumber();
            String companyEmail = faker.internet().emailAddress();
            String companySubdomain = faker.internet().domainName();

            if (numberOfCommonValues > successCount) {
                companyName += commonCompanyName;
            } else {
                companyName = companyName.replace(commonCompanyName, "");
            }

            String insertQuery =
                "INSERT INTO internal.tenant (company_name, email, phone, subdomain) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, companyName, companyPhone,
                    companyEmail,
                    companySubdomain);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }

            successCount++;
        }

        return successCount;
    }


    public Integer tenantUser(JdbcTemplate jdbcTemplate, UUID tenantId,
                              Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();


            String insertQuery =
                "INSERT INTO tenant.user (first_name, last_name, email, phone, tenant_id) VALUES (?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone, tenantId);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantUser(JdbcTemplate jdbcTemplate, UUID tenantId,
                              Integer count, String commonFirstName,
                              Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String firstName = faker.name().firstName();
            String lastName = faker.name().lastName();
            String email = faker.internet().emailAddress();
            String phone = faker.phoneNumber().phoneNumber();

            if (numberOfCommonValues > successCount) {
                firstName += commonFirstName;
            } else {
                firstName = firstName.replace(commonFirstName, "");
            }

            String insertQuery =
                "INSERT INTO tenant.user (first_name, last_name, email, phone, tenant_id) VALUES (?, ?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, firstName, lastName, email,
                    phone, tenantId);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalPermission(JdbcTemplate jdbcTemplate,
                                      Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String type = faker.name().username();
            String description = faker.text().text(50);

            String insertQuery =
                "INSERT INTO internal.authorization_permission (name, type, description) VALUES (?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, type, description);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalPermission(JdbcTemplate jdbcTemplate,
                                      Integer count,
                                      String commonName,
                                      Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String type = faker.name().username();
            String description = faker.text().text(50);

            if (numberOfCommonValues > successCount) {
                name += commonName;
            } else {
                name = name.replace(commonName, "");
            }

            String insertQuery =
                "INSERT INTO internal.authorization_permission (name, type, description) VALUES (?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, type, description);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantPermission(JdbcTemplate jdbcTemplate,
                                    UUID tenantId,
                                    Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String type = faker.name().username();
            String description = faker.text().text(50);

            String insertQuery =
                "INSERT INTO tenant.authorization_permission (name, type, description, tenant_id) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, type, description,
                    tenantId);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantPermission(JdbcTemplate jdbcTemplate,
                                    UUID tenantId,
                                    Integer count,
                                    String commonName,
                                    Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String type = faker.name().username();
            String description = faker.text().text(50);

            if (numberOfCommonValues > successCount) {
                name += commonName;
            } else {
                name = name.replace(commonName, "");
            }

            String insertQuery =
                "INSERT INTO tenant.authorization_permission (name, type, description, tenant_id) VALUES (?, ?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, type, description,
                    tenantId);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantRole(JdbcTemplate jdbcTemplate, UUID tenantId,
                              Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String description = faker.text().text(50);

            String insertQuery =
                "INSERT INTO tenant.authorization_role (name, description, tenant_id) VALUES (?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, description, tenantId);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer tenantRole(JdbcTemplate jdbcTemplate, UUID tenantId,
                              Integer count,
                              String commonName,
                              Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String description = faker.text().text(50);

            if (numberOfCommonValues > successCount) {
                name += commonName;
            } else {
                name = name.replace(commonName, "");
            }

            String insertQuery =
                "INSERT INTO tenant.authorization_role (name, description, tenant_id) VALUES (?, ?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, description, tenantId);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalRole(JdbcTemplate jdbcTemplate,
                                Integer count) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String description = faker.text().text(50);

            String insertQuery =
                "INSERT INTO internal.authorization_role (name, description) VALUES (?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, description);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }

    public Integer internalRole(JdbcTemplate jdbcTemplate,
                                Integer count,
                                String commonName,
                                Integer numberOfCommonValues) {
        Faker faker = new Faker();

        int successCount = 0;
        for (int i = 0; i < count; i++) {
            String name = faker.name().name();
            String description = faker.text().text(50);

            if (numberOfCommonValues > successCount) {
                name += commonName;
            } else {
                name = name.replace(commonName, "");
            }

            String insertQuery =
                "INSERT INTO internal.authorization_role (name, description) VALUES (?, ?)";
            try {
                jdbcTemplate.update(insertQuery, name, description);
            } catch (Exception exception) {
                // We could fail from unique values and will need to try again
                count++;

                System.out.println(exception.getMessage());
                continue;
            }
            successCount++;
        }

        return successCount;
    }
}
