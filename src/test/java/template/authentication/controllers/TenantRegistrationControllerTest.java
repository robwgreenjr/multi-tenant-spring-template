package template.authentication.controllers;

import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TenantRegistrationControllerTest extends IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"classpath:sql/truncateAllSchemas.sql"})
    public void givenValidData_whenTenantRegistered_shouldCreateTenant() {
        JSONObject data = new JSONObject();
        data.put("companyName", "Test Company");
        data.put("email", "tester.company@testme.io");
        data.put("subdomain", "testme");

        ResponseEntity<String> response =
            restTemplate.exchange("/tenant/registration",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        List<Map<String, Object>> tenantList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE subdomain = ?;", "testme");

        assertEquals(1, tenantList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateAllSchemas.sql"})
    public void givenValidData_whenTenantRegistered_shouldCreateTenantConfirmation() {
        JSONObject data = new JSONObject();
        data.put("companyName", "Test Company");
        data.put("email", "tester.company@testme.io");
        data.put("subdomain", "testme");

        restTemplate.exchange("/tenant/registration",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        List<Map<String, Object>> tenantList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE subdomain = ?;", "testme");

        List<Map<String, Object>> tenantConfirmationList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant_email_confirmation WHERE tenant_id = ?;",
                tenantList.get(0).get("id"));

        assertEquals(1, tenantConfirmationList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateAllSchemas.sql"})
    public void givenNoEmail_whenTenantRegistered_shouldReturnBadRequest() {
        JSONObject data = new JSONObject();
        data.put("companyName", "Test Company");
        data.put("subdomain", "testme");

        ResponseEntity<String> response =
            restTemplate.exchange("/tenant/registration",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateAllSchemas.sql"})
    public void givenValidData_whenRegistrationConfirmed_shouldCreateTenantUser() {
        JSONObject data = new JSONObject();
        data.put("companyName", "Test Company");
        data.put("email", "tester.company@testme.io");
        data.put("subdomain", "testme");

        restTemplate.exchange("/tenant/registration",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        List<Map<String, Object>> tenantList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE subdomain = ?;", "testme");

        List<Map<String, Object>> tenantConfirmationList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant_email_confirmation WHERE tenant_id = ?;",
                tenantList.get(0).get("id"));

        data = new JSONObject();
        data.put("token", tenantConfirmationList.get(0).get("token"));
        data.put("email", "owner@testme.io");
        data.put("firstName", "Owner");

        restTemplate.exchange("/tenant/registration/confirmation",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        List<Map<String, Object>> tenantUserList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE email = ?;",
                "owner@testme.io");
        assertEquals(1, tenantUserList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateAllSchemas.sql"})
    public void givenValidData_whenRegistrationConfirmed_shouldAssignRolesAndPermissions() {
        JSONObject data = new JSONObject();
        data.put("companyName", "Test Company");
        data.put("email", "tester.company@testme.io");
        data.put("subdomain", "testme");

        restTemplate.exchange("/tenant/registration",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        List<Map<String, Object>> tenantList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE subdomain = ?;", "testme");

        List<Map<String, Object>> tenantConfirmationList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant_email_confirmation WHERE tenant_id = ?;",
                tenantList.get(0).get("id"));

        data = new JSONObject();
        data.put("token", tenantConfirmationList.get(0).get("token"));
        data.put("email", "owner@testme.io");
        data.put("firstName", "Owner");

        restTemplate.exchange("/tenant/registration/confirmation",
            HttpMethod.POST,
            new HttpEntity<>(data.toString(), headers),
            String.class);

        List<Map<String, Object>> tenantRoleList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_role WHERE tenant_id = ?;",
                tenantList.get(0).get("id"));
        assertNotEquals(0, tenantRoleList.size());

        List<Map<String, Object>> tenantPermissionList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE tenant_id = ?;",
                tenantList.get(0).get("id"));
        assertNotEquals(0, tenantPermissionList.size());

        List<Map<String, Object>> tenantUserList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE email = ?;",
                "owner@testme.io");

        List<Map<String, Object>> tenantRoleUserList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_role_user WHERE user_id = ?;",
                tenantUserList.get(0).get("id"));
        assertEquals(1, tenantRoleUserList.size());
    }
}
