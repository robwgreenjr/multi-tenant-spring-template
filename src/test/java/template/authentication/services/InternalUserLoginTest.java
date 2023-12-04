package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.PasswordNotSetException;
import template.authentication.models.InternalUserPassword;
import template.authentication.models.Jwt;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;
import template.internal.models.InternalUser;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class InternalUserLoginTest extends IntegrationTest {

    @Autowired
    private InternalUserLogin simpleUserLogin;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserPasswordManager<InternalUserPassword> userPasswordManager;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUserPasswordExists_whenJwtProvider_shouldReturnJwtToken() {
        seeder.internalUser(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        InternalUser user = new InternalUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);

        Jwt actual =
            simpleUserLogin.jwtProvider(
                objectList.get(0).get("email").toString(), "password");

        assertNotNull(actual.getToken());
    }

    @Test(expected = PasswordNotSetException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUserPasswordNotSet_whenJwtProvider_shouldThrowException() {
        simpleUserLogin.jwtProvider("testing1@gmail.com", "password");
    }

    @Test(expected = PasswordIncorrectException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenWrongPassword_whenJwtProvider_shouldThrowException() {
        seeder.internalUser(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        InternalUser user = new InternalUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);

        simpleUserLogin.jwtProvider(objectList.get(0).get("email").toString(),
            "passwords");
    }
}
