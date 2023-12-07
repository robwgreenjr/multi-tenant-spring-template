package template.helpers;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import template.database.cli.Seeder;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import template.global.constants.GlobalVariable;
import template.global.services.StringEncoder;
import template.tenants.resolvers.TenantIdentifierResolver;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Import(IntegrationTest.TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Ignore("Used to wire up integration test.")
public class IntegrationTest {
    @ClassRule
    public static PostgreSQLContainer<TemplatePostgreSqlContainer>
        postgreSQLContainer =
        TemplatePostgreSqlContainer.getInstance();
    protected UUID tenantId;
    protected HttpHeaders headers = new HttpHeaders();
    @Autowired
    protected JdbcTemplate jdbcTemplate;
    @Qualifier("CryptoEncoder")
    @Autowired
    private StringEncoder cryptoEncoder;
    @Autowired
    private Seeder seeder;
    @Autowired
    private TenantIdentifierResolver currentTenant;

    @Before
    public void init() {
        tenantId = seeder.createMainTenant(jdbcTemplate);
        currentTenant.setCurrentTenant(tenantId);

        headers.set("tenant-id", String.valueOf(tenantId));
        headers.setContentType(MediaType.APPLICATION_JSON);

        seeder.defaultConfiguration(jdbcTemplate);
    }

    protected JdbcTemplate getTenantDataSource(String tenantId) {
        List<Map<String, Object>> tenantDatabase =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant_database WHERE tenant_id = '" +
                    tenantId + "'");
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setDriverClassName(
            postgreSQLContainer.getDriverClassName());
        dataSource.setJdbcUrl(tenantDatabase.get(0).get("url").toString());
        dataSource.setUsername(
            cryptoEncoder.decode(
                tenantDatabase.get(0).get("username").toString()));
        dataSource.setPassword(
            cryptoEncoder.decode(
                tenantDatabase.get(0).get("password").toString()));
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(2);

        return new JdbcTemplate(dataSource);
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public DataSource dataSource() {
            System.setProperty(GlobalVariable.DATABASE_DRIVER_CLASS_NAME,
                postgreSQLContainer.getDriverClassName());
            System.setProperty(GlobalVariable.DATABASE_URL,
                postgreSQLContainer.getJdbcUrl());
            System.setProperty(GlobalVariable.DATABASE_USER,
                postgreSQLContainer.getUsername());
            System.setProperty(GlobalVariable.DATABASE_PASSWORD,
                postgreSQLContainer.getPassword());

            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
            dataSource.setUsername(postgreSQLContainer.getUsername());
            dataSource.setPassword(postgreSQLContainer.getPassword());
            dataSource.setMinimumIdle(1);
            dataSource.setMaximumPoolSize(2);

            ApplicationDataSource applicationDataSource =
                new ApplicationDataSource();
            applicationDataSource.setDataSource(dataSource);
            DatabaseConnectionContext.setCurrentDatabaseConnection(
                applicationDataSource);

            return dataSource;
        }
    }
}