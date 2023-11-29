package template.global.controllers;

import template.helpers.IntegrationTest;
import org.json.JSONException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;

import static org.junit.Assert.assertEquals;

public class HealthCheckControllerTest extends IntegrationTest {
    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void whenCallingHealthCheck_ShouldRespond200()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();

        HttpEntity<String> entity = new HttpEntity<>(headers);
        ResponseEntity<String> response =
            restTemplate.exchange("/health-check", HttpMethod.GET, entity,
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }
}
