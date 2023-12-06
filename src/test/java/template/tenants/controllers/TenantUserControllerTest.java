package template.tenants.controllers;

import org.json.JSONArray;
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
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class TenantUserControllerTest extends IntegrationTest {
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
        seeder.tenantUser(jdbcTemplate, tenantId, 12);

        ResponseEntity<String> response =
            restTemplate.exchange("/users", HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(12, dataArray.length());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDataExists_whenGetSingleIsCalled_shouldReturnData() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/user/" + singleObject.get(0).get("id"),
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject actual = (JSONObject) dataArray.get(0);

        assertEquals(1, dataArray.length());
        assertEquals(singleObject.get(0).get("first_name"),
            actual.get("firstName"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenInvalidData_whenGetSingleIsCalled_shouldReturn404() {
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/user/1",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPost_shouldCreateData() {
        JSONObject data = new JSONObject();
        data.put("firstName", "Tester");
        data.put("lastName", "Blue");
        data.put("email", "tester.blue@test.io");
        data.put("phone", "555-555-5555");

        ResponseEntity<String> response =
            restTemplate.exchange("/user",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE email LIKE 'tester.blue@test.io'");
        assertEquals("Tester", singleObject.get(0).get("first_name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenNoEmail_whenPost_shouldReturn400() {
        JSONObject data = new JSONObject();
        data.put("firstName", "Tester");
        data.put("lastName", "Blue");
        data.put("phone", "555-555-5555");

        ResponseEntity<String> response =
            restTemplate.exchange("/user",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPostList_shouldCreateAllData() {
        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("firstName", "Tester");
        firstObject.put("lastName", "Blue");
        firstObject.put("email", "tester.blue@test.io");
        firstObject.put("phone", "555-555-5551");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("firstName", "Tester");
        secondObject.put("lastName", "Green");
        secondObject.put("email", "tester.green@test.io");
        secondObject.put("phone", "555-555-5552");
        data.put(secondObject);
        JSONObject thirdObject = new JSONObject();
        thirdObject.put("firstName", "Tester");
        thirdObject.put("lastName", "Orange");
        thirdObject.put("email", "tester.orange@test.io");
        thirdObject.put("phone", "555-555-5553");
        data.put(thirdObject);

        ResponseEntity<String> response =
            restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE email = 'tester.blue@test.io'");
        assertEquals("Blue", singleObject.get(0).get("last_name"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.user WHERE email = 'tester.green@test.io'");
        assertEquals("Green", singleObject.get(0).get("last_name"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.user WHERE email = 'tester.orange@test.io'");
        assertEquals("Orange", singleObject.get(0).get("last_name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDuplicateEmail_whenPostList_shouldReturn400() {
        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("firstName", "Tester");
        firstObject.put("lastName", "Blue");
        firstObject.put("email", "tester.blue@test.io");
        firstObject.put("phone", "555-555-5551");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("firstName", "Tester");
        secondObject.put("lastName", "Green");
        secondObject.put("email", "tester.blue@test.io");
        secondObject.put("phone", "555-555-5552");
        data.put(secondObject);

        ResponseEntity<String> response =
            restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDuplicatePhone_whenPostList_shouldReturn400() {
        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("firstName", "Tester");
        firstObject.put("lastName", "Blue");
        firstObject.put("email", "tester.blue@test.io");
        firstObject.put("phone", "555-555-5551");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("firstName", "Tester");
        secondObject.put("lastName", "Green");
        secondObject.put("email", "tester.green@test.io");
        secondObject.put("phone", "555-555-5551");
        data.put(secondObject);

        ResponseEntity<String> response =
            restTemplate.exchange("/users",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPut_shouldCreateData() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        JSONObject data = new JSONObject();
        data.put("firstName", "Updated");
        data.put("lastName", "Updated");
        data.put("email", "updated@test.io");
        data.put("phone", "555-555-5555");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/user/" + singleObject.get(0).get("id"),
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE email = 'updated@test.io'");
        assertEquals("Updated", singleObject.get(0).get("first_name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenPartialData_whenPut_shouldReturn400() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user");

        JSONObject data = new JSONObject();
        data.put("firstName", "Updated");
        data.put("lastName", "Updated");
        data.put("phone", "555-555-5555");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/user/" + singleObject.get(0).get("id"),
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenValidData_whenPutList_shouldCreateAllData() {
        seeder.tenantUser(jdbcTemplate, tenantId, 3);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("id", objectList.get(0).get("id"));
        firstObject.put("firstName", "Tester");
        firstObject.put("lastName", "Blue");
        firstObject.put("email", "tester.blue@test.io");
        firstObject.put("phone", "555-555-5551");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("id", objectList.get(1).get("id"));
        secondObject.put("firstName", "Tester");
        secondObject.put("lastName", "Green");
        secondObject.put("email", "tester.green@test.io");
        secondObject.put("phone", "555-555-5552");
        data.put(secondObject);
        JSONObject thirdObject = new JSONObject();
        thirdObject.put("id", objectList.get(2).get("id"));
        thirdObject.put("firstName", "Tester");
        thirdObject.put("lastName", "Orange");
        thirdObject.put("email", "tester.orange@test.io");
        thirdObject.put("phone", "555-555-5553");
        data.put(thirdObject);

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/users",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE email = 'tester.blue@test.io'");
        assertEquals("Blue", singleObject.get(0).get("last_name"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.user WHERE email = 'tester.green@test.io'");
        assertEquals("Green", singleObject.get(0).get("last_name"));

        singleObject = jdbcTemplate.queryForList(
            "SELECT * FROM tenant.user WHERE email = 'tester.orange@test.io'");
        assertEquals("Orange", singleObject.get(0).get("last_name"));
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDuplicateEmail_whenPutList_shouldReturn400() {
        seeder.tenantUser(jdbcTemplate, tenantId, 3);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("id", objectList.get(0).get("id"));
        firstObject.put("firstName", "Tester");
        firstObject.put("lastName", "Blue");
        firstObject.put("email", "tester.blue@test.io");
        firstObject.put("phone", "555-555-5551");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("id", objectList.get(1).get("id"));
        secondObject.put("firstName", "Tester");
        secondObject.put("lastName", "Green");
        secondObject.put("email", "tester.blue@test.io");
        secondObject.put("phone", "555-555-5552");
        data.put(secondObject);

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/users",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDuplicatePhone_whenPutList_shouldReturn400() {
        seeder.tenantUser(jdbcTemplate, tenantId, 3);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        JSONArray data = new JSONArray();
        JSONObject firstObject = new JSONObject();
        firstObject.put("id", objectList.get(0).get("id"));
        firstObject.put("firstName", "Tester");
        firstObject.put("lastName", "Blue");
        firstObject.put("email", "tester.blue@test.io");
        firstObject.put("phone", "555-555-5551");
        data.put(firstObject);
        JSONObject secondObject = new JSONObject();
        secondObject.put("id", objectList.get(1).get("id"));
        secondObject.put("firstName", "Tester");
        secondObject.put("lastName", "Green");
        secondObject.put("email", "tester.green@test.io");
        secondObject.put("phone", "555-555-5551");
        data.put(secondObject);

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/users",
                HttpMethod.PUT,
                new HttpEntity<>(data.toString(), headers),
                String.class);


        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenPartialData_whenPatch_shouldUpdate() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        JSONObject data = new JSONObject();
        data.put("firstName", "Partial Update");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/user/" + objectList.get(0).get("id"),
                HttpMethod.PATCH,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE first_name = 'Partial Update'");
        assertEquals(1, singleObject.size());
    }

    @Test
    @Sql(scripts = {
        "classpath:sql/truncateTenantSchema.sql"})
    public void givenDataExists_whenDelete_shouldRemoveObject() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        ResponseEntity<String> response =
            restTemplate.exchange(
                "/user/" + objectList.get(0).get("id"),
                HttpMethod.DELETE,
                new HttpEntity<>(headers),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        List<Map<String, Object>> singleObject =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user WHERE first_name = '" +
                    objectList.get(0).get("first_name") + "'");
        assertEquals(0, singleObject.size());
    }
}
