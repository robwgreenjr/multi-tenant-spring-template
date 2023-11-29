package template.global.controllers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.global.entities.Configuration;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class ConfigurationControllerTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @Sql(scripts = {"classpath:sql/global/configuration/create.sql"})
    public void givenConfigurationExist_whenCalledByKey_shouldReturnEntity()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/configuration/JWT_SECRET", HttpMethod.GET,
                entity,
                String.class);

        assertNotNull(response.getBody());

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject data = (JSONObject) dataArray.get(0);

        assertEquals("this is a test", data.get("value"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/global/configuration/create.sql"})
    public void givenUpdatePartialName_whenCallingPATCH_shouldUpdateUserName() {
        String expected = "this is a test partial";

        Configuration configuration = new Configuration();
        configuration.setKey("JWT_SECRET");
        configuration.setValue(expected);

        HttpHeaders headers = new HttpHeaders();

        restTemplate.exchange("/configuration", HttpMethod.PATCH,
            new HttpEntity<>(configuration, headers),
            String.class);

        List<Map<String, Object>> jwtSecret =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.configuration WHERE key = 'JWT_SECRET'");

        assertEquals(expected, jwtSecret.get(0).get("value"));
    }
}
