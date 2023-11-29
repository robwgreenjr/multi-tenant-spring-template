package template.global.controllers;

import com.zaxxer.hikari.HikariDataSource;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import template.helpers.DatabaseSeeder;
import template.helpers.TemplatePostgreSqlContainer;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

import static org.junit.Assert.assertEquals;

@Import(ParameterControllerTest.TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ParameterControllerTest {
    @ClassRule
    public static PostgreSQLContainer<TemplatePostgreSqlContainer>
        postgreSQLContainer =
        TemplatePostgreSqlContainer.getInstance();
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void whenCallingTests_ShouldRespond200()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/tests", HttpMethod.GET, entity,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenLikeFilter_shouldReturnCorrectData()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 100, "test", 15);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?stringColumn[like]=test", HttpMethod.GET,
            entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(15, dataArray.length());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenInvalidLimitSize_shouldReturn409()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?limit=400", HttpMethod.GET,
            entity, String.class);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenGreaterThanFilter_whenInteger_shouldReturnCorrectData()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 100);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?id[gt]=50", HttpMethod.GET,
            entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(50, dataArray.length());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenGreaterThanOrEqualToFilter_whenInteger_shouldReturnCorrectData()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 100);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?id[gte]=50", HttpMethod.GET,
            entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(51, dataArray.length());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenLessThanOrEqualToFilter_whenInteger_shouldReturnCorrectData()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 100);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?id[lte]=50", HttpMethod.GET,
            entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(50, dataArray.length());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenLessThanFilter_whenInteger_shouldReturnCorrectData()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 100);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?id[lt]=50", HttpMethod.GET,
            entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(49, dataArray.length());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenMultipleFilters_shouldReturnCorrectData()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 100, "test", 15);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?stringColumn[like]=test&id[gt]=2",
            HttpMethod.GET, entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("data");

        assertEquals(13, dataArray.length());
    }

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public DataSource dataSource() {
            HikariDataSource dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl());
            dataSource.setUsername(postgreSQLContainer.getUsername());
            dataSource.setPassword(postgreSQLContainer.getPassword());
            dataSource.setMinimumIdle(1);
            dataSource.setMaximumPoolSize(2);

            JdbcTemplate testDatabase = new JdbcTemplate(dataSource);
            testDatabase.execute("CREATE DATABASE parameter_controller;");

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl()
                .replace("test", "parameter_controller"));
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
