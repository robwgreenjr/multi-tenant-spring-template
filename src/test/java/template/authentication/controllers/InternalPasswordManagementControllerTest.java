package template.authentication.controllers;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.InternalUserLogin;
import template.database.cli.Seeder;
import template.helpers.InternalIntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class InternalPasswordManagementControllerTest extends
    InternalIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private InternalUserLogin simpleUserLogin;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenValidData_whenCallingPut_shouldChangePassword() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        JSONObject data = new JSONObject();
        data.put("password", "passwords");
        data.put("passwordConfirmation", "passwords");
        data.put("emailConfirmation",
            objectList.get(0).get("email").toString());
        data.put("currentPassword", "password");

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/password",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        InternalUserPassword actual =
            simpleUserLogin.login(objectList.get(0).get("email").toString(),
                "passwords");
        assertNotNull(actual.getPassword());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenValidData_whenForgotCalled_shouldCreateResetPassword() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user;");

        JSONObject data = new JSONObject();
        data.put("email", objectList.get(0).get("email").toString());

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/password/forgot",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> actual =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token " +
                    "JOIN internal.user u on u.id = authentication_user_reset_password_token.user_id WHERE u.email = '" +
                    objectList.get(0).get("email").toString() + "'");
        assertFalse(actual.isEmpty());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenValidData_whenResetCalled_shouldUpdatePassword() {
        seeder.internalUserPasswordAndResetPasswordToken(jdbcTemplate, 1);
        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token");
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");
        JSONObject data = new JSONObject();
        data.put("token",
            resetPasswordTokenList.get(0).get("token").toString());
        data.put("password", "passwords");
        data.put("passwordConfirmation", "passwords");

        restTemplate.exchange("/internal/authentication/password/reset",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        InternalUserPassword actual =
            simpleUserLogin.login(userList.get(0).get("email").toString(),
                "passwords");
        assertNotNull(actual.getPassword());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNoUserPassword_whenResetCalled_shouldCreatePassword() {
        seeder.internalUserPasswordAndResetPasswordToken(jdbcTemplate, 1);
        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token");
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");
        JSONObject data = new JSONObject();
        data.put("token",
            resetPasswordTokenList.get(0).get("token").toString());
        data.put("password", "passwords");
        data.put("passwordConfirmation", "passwords");

        restTemplate.exchange("/internal/authentication/password/reset",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        InternalUserPassword actual =
            simpleUserLogin.login(userList.get(0).get("email").toString(),
                "passwords");
        assertNotNull(actual.getPassword());
    }
}
