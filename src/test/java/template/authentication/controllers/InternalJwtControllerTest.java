package template.authentication.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.constants.AuthenticationVariable;
import template.authentication.models.InternalUserPassword;
import template.authentication.models.Jwt;
import template.authentication.services.UserLoginHandler;
import template.database.cli.Seeder;
import template.helpers.InternalIntegrationTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class InternalJwtControllerTest extends InternalIntegrationTest {
    @Autowired
    @Qualifier("InternalUserLogin")
    private UserLoginHandler<InternalUserPassword> simpleUserLogin;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenLoginDetails_whenGenerateCalled_shouldReturnToken()
        throws JSONException {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        JSONObject data = new JSONObject();
        data.put("email", objectList.get(0).get("email"));
        data.put("password", "password");

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/jwt",
                HttpMethod.POST,
                new HttpEntity<>(data.toString(), headers),
                String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject actual = (JSONObject) dataArray.get(0);

        assertNotNull(actual.get("token"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenWrongLoginDetails_whenGenerateCalled_shouldReturn401() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user;");

        JSONObject data = new JSONObject();
        data.put("email", objectList.get(0).get("email"));
        data.put("password", "wrong password");

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/jwt",
                HttpMethod.POST, new HttpEntity<>(data.toString(), headers),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenValidJWT_whenValidateCalled_shouldReturnClaims()
        throws JSONException {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        Jwt jwt =
            simpleUserLogin.jwtProvider(
                objectList.get(0).get("email").toString(), "password");

        headers.setBearerAuth(jwt.getToken());

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/jwt",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject data = (JSONObject) dataArray.get(0);

        assertEquals(objectList.get(0).get("email").toString(),
            data.get("sub"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenInvalidJWT_whenValidateCalled_shouldReturn400()
        throws JSONException {
        headers.setBearerAuth("invalid");

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/jwt",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenExpiredJWT_whenValidateCalled_shouldReturn401() {
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.configuration WHERE key = ?",
                AuthenticationVariable.JWT_SECRET);

        LocalDateTime expiration = LocalDateTime.now().minusMinutes(150);
        String jwt = Jwts.builder().setSubject("testing1@gmail.com")
            .signWith(SignatureAlgorithm.HS256,
                objectList.get(0).get("value").toString())
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(
                Date.from(expiration.toInstant(ZoneOffset.ofHours(-5))))
            .compact();

        headers.setBearerAuth(jwt);

        ResponseEntity<String> response =
            restTemplate.exchange("/internal/authentication/jwt",
                HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
