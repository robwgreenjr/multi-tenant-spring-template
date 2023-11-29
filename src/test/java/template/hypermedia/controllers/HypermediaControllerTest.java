package template.hypermedia.controllers;

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
import org.springframework.http.*;
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

@Import(HypermediaControllerTest.TestConfig.class)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class HypermediaControllerTest {
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
    public void whenCallingEndpoint_shouldReturnSelfLink()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");
        JSONObject selfLinks = (JSONObject) links.get("self");

        assertNotNull(selfLinks);
        assertEquals(restTemplate.getRootUri() + "/single-tables",
            selfLinks.get("href"));
        assertEquals("GET", selfLinks.get("type"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenEndpointError_shouldReturnSelfLink()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/endpoint-error", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");
        JSONObject selfLinks = (JSONObject) links.get("self");

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR,
            response.getStatusCode());
        assertNotNull(selfLinks);
        assertEquals(restTemplate.getRootUri() + "/endpoint-error",
            selfLinks.get("href"));
        assertEquals("GET", selfLinks.get("type"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenValidDataAmount_whenCallingQueryEndpoint_shouldReturnNextLink()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");

        assertTrue(links.has("next"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenDataDoesntExceedLimit_whenCallingEndpoint_shouldNotReturnNextLink()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 50);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");

        assertFalse(links.has("next"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenCursorProvided_whenCallingQueryEndpoint_shouldReturnPreviousLink()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 400);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?id[cursor]=201",
                HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");

        assertTrue(links.has("previous"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void givenNoCursorProvided_whenCallingQueryEndpoint_shouldNotReturnPreviousLink()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 200);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");

        assertFalse(links.has("previous"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenTraversingEntireDataSet_shouldAllowForAllToBeViewed()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 1043);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables", HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");
        JSONObject nextLink = (JSONObject) links.get("next");
        JSONArray dataArray = (JSONArray) result.get("data");

        int count = dataArray.length();
        boolean hasNext = true;
        while (hasNext) {
            response =
                restTemplate.exchange(nextLink.get("href").toString(),
                    HttpMethod.GET,
                    entity, String.class);
            result = new JSONObject(response.getBody());
            links = (JSONObject) result.get("links");
            dataArray = (JSONArray) result.get("data");

            count += dataArray.length();

            if (!links.has("next")) {
                hasNext = false;
            } else {
                nextLink = (JSONObject) links.get("next");
            }
        }

        assertEquals(1043, count);
    }

    @Test
    @Sql(scripts = {"classpath:sql/dropTables.sql",
        "classpath:sql/database/queryBuilder.sql"})
    public void whenTraversingEntireDataSetBackwards_shouldAllowForAllToBeViewed()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        DatabaseSeeder databaseSeeder = new DatabaseSeeder();
        databaseSeeder.singleTable(jdbcTemplate, 765);

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/single-tables?id[cursor]=765",
                HttpMethod.GET,
                entity, String.class);

        JSONObject result = new JSONObject(response.getBody());
        JSONObject links = (JSONObject) result.get("links");
        JSONObject previousLink = (JSONObject) links.get("previous");
        JSONArray dataArray = (JSONArray) result.get("data");

        int count = dataArray.length();
        boolean hasPrevious = true;
        while (hasPrevious) {
            response =
                restTemplate.exchange(previousLink.get("href").toString(),
                    HttpMethod.GET,
                    entity, String.class);
            result = new JSONObject(response.getBody());
            links = (JSONObject) result.get("links");
            dataArray = (JSONArray) result.get("data");

            count += dataArray.length();

            if (!links.has("previous")) {
                hasPrevious = false;
            } else {
                previousLink = (JSONObject) links.get("previous");
            }
        }

        assertEquals(765, count);
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
            testDatabase.execute("CREATE DATABASE hypermedia_controller;");

            dataSource = new HikariDataSource();
            dataSource.setDriverClassName(
                postgreSQLContainer.getDriverClassName());
            dataSource.setJdbcUrl(postgreSQLContainer.getJdbcUrl()
                .replace("test", "hypermedia_controller"));
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
