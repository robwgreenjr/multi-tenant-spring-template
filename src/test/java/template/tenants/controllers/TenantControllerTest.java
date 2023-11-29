package template.tenants.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.database.cli.Seeder;
import template.helpers.InternalIntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TenantControllerTest extends InternalIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenDataExists_whenGetListIsCalled_shouldReturnData() {
        seeder.tenantTable(jdbcTemplate, 12);

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/tenants", HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(12, dataArray.length());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenDataExists_whenGetSingleIsCalled_shouldReturnData() {
        seeder.tenantTable(jdbcTemplate, 3, "test", 1);

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE company_name LIKE '%test%'");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/internal/tenant/" + singleObject.get(0).get("id"),
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject actual = (JSONObject) dataArray.get(0);

        assertEquals(1, dataArray.length());
        assertEquals(singleObject.get(0).get("company_name"),
            actual.get("companyName"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenInvalidData_whenGetSingleIsCalled_shouldReturn404() {
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/internal/tenant/44beebb8-da63-4beb-b975-194eb3f4d834",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenValidData_whenPost_shouldCreateData() {
        seeder.tenantTable(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.tenant");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("companyName", "Tester");
        data.put("subdomain", "Blue");
        data.put("email", "tester.blue@test.io");
        data.put("phone", "555-555-5555");
        data.put("tenantId", objectList.get(0).get("id"));

        ResponseEntity<String> response =
            restTemplate.postForEntity("/internal/tenant",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE email LIKE 'tester.blue@test.io'");
        assertEquals("Tester", singleObject.get(0).get("company_name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenNoEmail_whenPost_shouldReturn400() {
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("companyName", "Tester");
        data.put("subdomain", "Blue");
        data.put("phone", "555-555-5555");

        ResponseEntity<String> response =
            restTemplate.postForEntity("/internal/tenant",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenValidData_whenPut_shouldCreateData() {
        seeder.tenantTable(jdbcTemplate, 1);
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList("SELECT * FROM internal.tenant");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("companyName", "Updated");
        data.put("subdomain", "Updated");
        data.put("email", "updated@test.io");
        data.put("phone", "555-555-5555");
        data.put("tenantId", singleObject.get(0).get("tenant_id"));

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/internal/tenant/" + singleObject.get(0).get("id"),
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE email = 'updated@test.io'");
        assertEquals("Updated", singleObject.get(0).get("company_name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenPartialData_whenPut_shouldReturn400() {
        seeder.tenantTable(jdbcTemplate, 1);
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("companyName", "Updated");
        data.put("subdomain", "Updated");
        data.put("phone", "555-555-5555");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/internal/tenant/" + singleObject.get(0).get("id"),
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenPartialData_whenPatch_shouldUpdate() {
        seeder.tenantTable(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.tenant");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("companyName", "Partial Update");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/internal/tenant/" + objectList.get(0).get("id"),
                HttpMethod.PATCH,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE company_name = 'Partial Update'");
        assertEquals(1, singleObject.size());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenDataExists_whenDelete_shouldRemoveObject() {
        seeder.tenantTable(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.tenant");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/internal/tenant/" + objectList.get(0).get("id"),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.tenant WHERE company_name = '" +
                    objectList.get(0).get("first_name") + "'");
        assertEquals(0, singleObject.size());
    }
}
