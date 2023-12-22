package template.global.controllers;

import com.zaxxer.hikari.HikariDataSource;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;
import template.database.models.ApplicationDataSource;
import template.database.models.DatabaseConnectionContext;
import template.helpers.DatabaseSeeder;
import template.helpers.TemplatePostgreSqlContainer;

import javax.sql.DataSource;

import static org.junit.Assert.*;

@Import(template.global.controllers.MetaControllerTest.TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MetaControllerTest {
    @ClassRule
    public static PostgreSQLContainer<TemplatePostgreSqlContainer>
        postgreSQLContainer =
        TemplatePostgreSqlContainer.getInstance();
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingDefaultEndpoint_shouldReturnTimestamp()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/tests", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertNotNull(metaData.get("timestamp"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingQueryEndpoint_shouldReturnTimestamp()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertNotNull(metaData.get("timestamp"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingQueryEndpoint_shouldReturnPageCount()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(2, metaData.get("pageCount"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenLimitAmount_whenCallingQueryEndpoint_shouldReturnCorrectMetaPageCount()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 50, "test", 15);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/single-tables?stringColumn[like]=test&limit=2",
                HttpMethod.GET, entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(8, metaData.get("pageCount"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenDataHasLastPageWithLessThanLimitAmount_shouldReturnCorrectMetaPageCount()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 413);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(3, metaData.get("pageCount"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenNoData_whenCallingQueryEndpoint_shouldReturnZeroPage()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(0, metaData.get("page"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingQueryEndpoint_shouldReturnPage()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(1, metaData.get("page"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenCursor_whenCallingQueryEndpoint_shouldReturnPage()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?id[cursor]=201",
                HttpMethod.GET, entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(2, metaData.get("page"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenLastPageDoesNotHaveFullLimitAmount_whenCallingQueryEndpoint_shouldReturnCorrectMetaPage()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 50, "test", 15);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/single-tables?stringColumn[like]=test&limit=2&id[cursor]=15",
                HttpMethod.GET, entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(8, metaData.get("page"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenMultipleFilters_whenCallingQueryEndpoint_shouldReturnCorrectCount()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 100, "test", 15, "another", 45);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
            "/single-tables?stringColumn[like]=test&[or]textColumn[like]=another",
            HttpMethod.GET, entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(60, metaData.get("count"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingQueryEndpoint_shouldReturnTotalCount()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(400, metaData.get("count"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenCursor_whenCallingQueryEndpoint_shouldReturnTotalCount()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?id[cursor]=201",
                HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(400, metaData.get("count"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingQueryEndpoint_shouldReturnLimit()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(200, metaData.get("limit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenLimitChanges_shouldReturnChangedLimit()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 100);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?limit=50", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(50, metaData.get("limit"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenInvalidLimit_shouldReturnErrorMessage()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 100);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?limit=500", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONArray dataArray = (JSONArray) result.get("errors");

        assertEquals(1, dataArray.length());
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenValidDataAmountExists_whenCallingQueryEndpoint_shouldReturnNextCursor()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertTrue(metaData.has("next"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenLastPageCursor_whenCallingQueryEndpoint_shouldNotReturnNextCursor()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?id[cursor]=201",
                HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertTrue(metaData.isNull("next"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenCallingQueryEndpoint_shouldReturnCorrectMetaNextCursor()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 50, "test", 15);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange(
                "/single-tables?stringColumn[like]=test&limit=2",
                HttpMethod.GET, entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertEquals(3, metaData.get("next"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenThereIsNoPreviousList_whenCallingQueryEndpoint_shouldNotReturnPreviousCursor()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET, entity,
                String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertTrue(metaData.isNull("previous"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenPreviousList_whenCallingQueryEndpoint_shouldReturnPreviousCursor()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.single(jdbcTemplate, 600);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?id[cursor]=201",
                HttpMethod.GET, entity, String.class);
        JSONObject result = new JSONObject(response.getBody());
        JSONObject metaData = (JSONObject) result.get("meta");

        assertTrue(metaData.has("previous"));
        assertEquals(1, metaData.get("previous"));
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
            testDatabase.execute("CREATE DATABASE meta_controller;");

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl()
                .replace("test", "meta_controller"));
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
