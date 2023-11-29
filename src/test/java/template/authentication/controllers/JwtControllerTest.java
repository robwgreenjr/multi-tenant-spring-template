package template.authentication.controllers;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.dtos.SimpleUserLoginDto;
import template.authentication.models.Jwt;
import template.authentication.services.UserLoginHandler;
import template.authentication.services.UserPasswordManager;
import template.helpers.IntegrationTest;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JwtControllerTest extends IntegrationTest {
    @Autowired
    private UserLoginHandler simpleUserLogin;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserPasswordManager userPasswordManager;

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenLoginDetails_whenGenerateCalled_shouldReturnToken()
        throws JSONException {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(user);
//        userPasswordModel.setPassword("password");
//
//        userPasswordManager.create(userPasswordModel);

        SimpleUserLoginDto simpleUserLoginDto = new SimpleUserLoginDto();
        simpleUserLoginDto.email = "testing1@gmail.com";
        simpleUserLoginDto.password = "password";

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/jwt", HttpMethod.POST,
                new HttpEntity<>(simpleUserLoginDto),
                String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject data = (JSONObject) dataArray.get(0);

        assertNotNull(data.get("token"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenWrongLoginDetails_whenGenerateCalled_shouldReturn401() {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(user);
//        userPasswordModel.setPassword("password");
//
//        userPasswordManager.create(userPasswordModel);

        String uri = "/authentication/jwt";
        SimpleUserLoginDto simpleUserLoginDto = new SimpleUserLoginDto();
        simpleUserLoginDto.email = "testing1@gmail.com";
        simpleUserLoginDto.password = "wrong password";

        ResponseEntity<String> response =
            restTemplate.exchange(uri, HttpMethod.POST,
                new HttpEntity<>(simpleUserLoginDto),
                String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenValidJWT_whenValidateCalled_shouldReturnClaims()
        throws JSONException {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(user);
//        userPasswordModel.setPassword("password");
//
//        userPasswordManager.create(userPasswordModel);

        Jwt jwtModel =
            simpleUserLogin.jwtProvider("testing1@gmail.com", "password");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwtModel.getToken());

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/jwt", HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        JSONObject result = new JSONObject(response.getBody());

        JSONArray dataArray = (JSONArray) result.get("data");
        JSONObject data = (JSONObject) dataArray.get(0);

        assertEquals("testing1@gmail.com", data.get("sub"));
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenInvalidJWT_whenValidateCalled_shouldReturn400()
        throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("invalid");

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/jwt", HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenExpiredJWT_whenValidateCalled_shouldReturn401() {
        LocalDateTime expiration = LocalDateTime.now().minusMinutes(150);
        String jwt = Jwts.builder().setSubject("testing1@gmail.com")
            .signWith(SignatureAlgorithm.HS256, "this is a test")
            .setIssuedAt(new Date(System.currentTimeMillis()))
            .setExpiration(
                Date.from(expiration.toInstant(ZoneOffset.ofHours(-5))))
            .compact();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(jwt);

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/jwt", HttpMethod.GET,
                new HttpEntity<>(headers), String.class);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }
}
