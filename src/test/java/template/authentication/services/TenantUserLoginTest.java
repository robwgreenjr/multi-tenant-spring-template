package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.PasswordNotSetException;
import template.authentication.models.Jwt;
import template.authentication.models.TenantUserPassword;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;
import template.tenants.models.TenantUser;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class TenantUserLoginTest extends IntegrationTest {
    @Autowired
    @Qualifier("TenantUserLogin")
    private UserLoginHandler<TenantUserPassword> simpleUserLogin;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("TenantUserPasswordManager")
    private UserPasswordManager<TenantUserPassword> userPasswordManager;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUserPasswordExists_whenJwtProvider_shouldReturnJwtToken() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user;");

        Jwt actual =
            simpleUserLogin.jwtProvider(
                objectList.get(0).get("email").toString(), "password");

        assertNotNull(actual.getToken());
    }

    @Test(expected = PasswordNotSetException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUserPasswordNotSet_whenJwtProvider_shouldThrowException() {
        simpleUserLogin.jwtProvider("testing1@gmail.com", "password");
    }

    @Test(expected = PasswordIncorrectException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenWrongPassword_whenJwtProvider_shouldThrowException() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        TenantUser user = new TenantUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);

        simpleUserLogin.jwtProvider(objectList.get(0).get("email").toString(),
            "passwords");
    }
}
