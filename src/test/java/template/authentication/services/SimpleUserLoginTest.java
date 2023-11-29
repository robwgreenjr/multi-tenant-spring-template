package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.PasswordIncorrectException;
import template.authentication.exceptions.PasswordNotSetException;
import template.authentication.models.Jwt;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertNotNull;

public class SimpleUserLoginTest extends IntegrationTest {

    @Autowired
    private SimpleUserLogin simpleUserLogin;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserPasswordManager userPasswordManager;

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenUserPasswordExists_whenJwtProvider_shouldReturnJwtToken() {
        List<Map<String, Object>> userList = jdbcTemplate.queryForList(
            "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");

//        UserModel userModel = new UserModel();
//        userModel.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(userModel);
//        userPasswordModel.setPassword("password");
//
//        userPasswordManager.create(userPasswordModel);

        Jwt actual =
            simpleUserLogin.jwtProvider("testing1@gmail.com", "password");

        assertNotNull(actual.getToken());
    }

    @Test(expected = PasswordNotSetException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUserPasswordNotSet_whenJwtProvider_shouldThrowException() {
        simpleUserLogin.jwtProvider("testing1@gmail.com", "password");
    }

    @Test(expected = PasswordIncorrectException.class)
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenWrongPassword_whenJwtProvider_shouldThrowException() {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");

//        UserModel userModel = new UserModel();
//        userModel.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(userModel);
//        userPasswordModel.setPassword("password");
//
//        userPasswordManager.create(userPasswordModel);

        simpleUserLogin.jwtProvider("testing1@gmail.com", "passwords");
    }
}
