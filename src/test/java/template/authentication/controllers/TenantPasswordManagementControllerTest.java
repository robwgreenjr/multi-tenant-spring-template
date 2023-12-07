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
import template.authentication.models.TenantUserPassword;
import template.authentication.services.TenantUserLogin;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

public class TenantPasswordManagementControllerTest extends
    IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private TenantUserLogin simpleUserLogin;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenCallingPut_shouldChangePassword() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        JSONObject data = new JSONObject();
        data.put("password", "passwords");
        data.put("passwordConfirmation", "passwords");
        data.put("emailConfirmation",
            objectList.get(0).get("email").toString());
        data.put("currentPassword", "password");

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/password",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        TenantUserPassword actual =
            simpleUserLogin.login(objectList.get(0).get("email").toString(),
                "passwords");
        assertNotNull(actual.getPassword());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenForgotCalled_shouldCreateResetPassword() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user;");

        JSONObject data = new JSONObject();
        data.put("email", objectList.get(0).get("email").toString());

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/password/forgot",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> actual =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token " +
                    "JOIN tenant.user u on u.id = authentication_user_reset_password_token.user_id WHERE u.email = '" +
                    objectList.get(0).get("email").toString() + "'");
        assertFalse(actual.isEmpty());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenResetCalled_shouldUpdatePassword() {
        seeder.tenantUserPasswordAndResetPasswordToken(jdbcTemplate, tenantId,
            1);
        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token");
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");
        JSONObject data = new JSONObject();
        data.put("token",
            resetPasswordTokenList.get(0).get("token").toString());
        data.put("password", "passwords");
        data.put("passwordConfirmation", "passwords");

        restTemplate.exchange("/authentication/password/reset",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        TenantUserPassword actual =
            simpleUserLogin.login(userList.get(0).get("email").toString(),
                "passwords");
        assertNotNull(actual.getPassword());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNoUserPassword_whenResetCalled_shouldCreatePassword() {
        seeder.tenantUserPasswordAndResetPasswordToken(jdbcTemplate, tenantId,
            1);
        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token");
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");
        JSONObject data = new JSONObject();
        data.put("token",
            resetPasswordTokenList.get(0).get("token").toString());
        data.put("password", "passwords");
        data.put("passwordConfirmation", "passwords");

        restTemplate.exchange("/authentication/password/reset",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        TenantUserPassword actual =
            simpleUserLogin.login(userList.get(0).get("email").toString(),
                "passwords");
        assertNotNull(actual.getPassword());
    }
}
