package template.authorization.controllers;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;


public class TenantPermissionControllerTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDataExists_whenGetListIsCalled_shouldReturnData() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 12);

        ResponseEntity<String> response =
            restTemplate.exchange("/authorization/permissions",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(12, dataArray.length());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDataExists_whenGetSingleIsCalled_shouldReturnData() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 3, "test", 1);

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name LIKE '%test%'");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permission/" +
                    singleObject.get(0).get("id"),
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject actual = (JSONObject) dataArray.get(0);

        assertEquals(1, dataArray.length());
        assertEquals(singleObject.get(0).get("name"),
            actual.get("name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenInvalidData_whenGetSingleIsCalled_shouldReturn404() {
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permission/1",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPost_shouldCreateData() {
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("name", "Tester");
        data.put("type", "Blue");
        data.put("description", "This is some long description test.");
        ResponseEntity<String> response =
            restTemplate.postForEntity("/authorization/permission",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name = 'Tester'");
        assertEquals("Blue", singleObject.get(0).get("type"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenNoName_whenPost_shouldReturn400() {
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("type", "Blue");
        ResponseEntity<String> response =
            restTemplate.postForEntity("/authorization/permission",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenNoType_whenPost_shouldReturn400() {
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("name", "Tester");
        ResponseEntity<String> response =
            restTemplate.postForEntity("/authorization/permission",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPostList_shouldCreateAllData() {
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("name", "Tester");
        firstObject.put("type", "Blue");
        firstObject.put("description", "This is some long description test.");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("name", "TesterG");
        secondObject.put("type", "Green");
        secondObject.put("description", "This is some long description test.");
        data.put(secondObject);
        JSONObject thirdObject = new JSONObject();
        thirdObject.put("name", "TesterO");
        thirdObject.put("type", "Orange");
        thirdObject.put("description", "This is some long description test.");
        data.put(thirdObject);

        ResponseEntity<String> response =
            restTemplate.postForEntity("/authorization/permissions",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name = 'Tester'");
        assertEquals("Blue", singleObject.get(0).get("type"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.authorization_permission WHERE name = 'TesterG'");
        assertEquals("Green", singleObject.get(0).get("type"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.authorization_permission WHERE name = 'TesterO'");
        assertEquals("Orange", singleObject.get(0).get("type"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDuplicateNameAndType_whenPostList_shouldReturn400() {
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("name", "Tester");
        firstObject.put("type", "Blue");
        firstObject.put("description", "This is some long description test.");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("name", "Tester");
        secondObject.put("type", "Blue");
        secondObject.put("description", "This is some long description test.");
        data.put(secondObject);

        ResponseEntity<String> response =
            restTemplate.postForEntity("/authorization/permissions",
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPut_shouldCreateData() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("name", "Updated Name");
        data.put("type", "Updated Type");
        data.put("description", "This is some long description test.");
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permission/" +
                    singleObject.get(0).get("id"),
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name = 'Updated Name'");
        assertEquals("Updated Type", singleObject.get(0).get("type"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenPartialData_whenPut_shouldReturn400() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("name", "Updated");
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permission/" +
                    singleObject.get(0).get("id"),
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPutList_shouldCreateAllData() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 3);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("id", objectList.get(0).get("id"));
        firstObject.put("name", "Tester");
        firstObject.put("type", "Blue");
        firstObject.put("description", "This is some long description test.");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("id", objectList.get(1).get("id"));
        secondObject.put("name", "TesterG");
        secondObject.put("type", "Green");
        secondObject.put("description", "This is some long description test.");
        data.put(secondObject);
        JSONObject thirdObject = new JSONObject();
        thirdObject.put("id", objectList.get(2).get("id"));
        thirdObject.put("name", "TesterO");
        thirdObject.put("type", "Orange");
        thirdObject.put("description", "This is some long description test.");
        data.put(thirdObject);

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permissions",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name = 'Tester'");
        assertEquals("Blue", singleObject.get(0).get("type"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.authorization_permission WHERE name = 'TesterG'");
        assertEquals("Green", singleObject.get(0).get("type"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.authorization_permission WHERE name = 'TesterO'");
        assertEquals("Orange", singleObject.get(0).get("type"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDuplicateNameAndType_whenPutList_shouldReturn400() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 3);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("id", objectList.get(0).get("id"));
        firstObject.put("name", "Tester");
        firstObject.put("type", "Blue");
        firstObject.put("description", "This is some long description test.");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("id", objectList.get(1).get("id"));
        secondObject.put("name", "Tester");
        secondObject.put("type", "Blue");
        secondObject.put("description", "This is some long description test.");
        data.put(secondObject);

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permissions",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenPartialData_whenPatch_shouldUpdate() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission");

        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject data = new JSONObject();
        data.put("name", "Partial Update");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permission/" +
                    objectList.get(0).get("id"),
                HttpMethod.PATCH,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name = 'Partial Update'");
        assertEquals(1, singleObject.size());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDataExists_whenDelete_shouldRemoveObject() {
        seeder.tenantPermission(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/authorization/permission/" +
                    objectList.get(0).get("id"),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authorization_permission WHERE name = '" +
                    objectList.get(0).get("name") + "'");
        assertEquals(0, singleObject.size());
    }
}