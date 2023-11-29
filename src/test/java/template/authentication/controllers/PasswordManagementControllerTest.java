package template.authentication.controllers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.dtos.ChangePasswordDto;
import template.authentication.dtos.ForgotPasswordDto;
import template.authentication.models.InternalResetPasswordToken;
import template.authentication.models.InternalUserPassword;
import template.authentication.services.ResetPasswordTokenManager;
import template.authentication.services.SimpleUserLogin;
import template.authentication.services.UserPasswordManager;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class PasswordManagementControllerTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private UserPasswordManager userPasswordManager;
    @Autowired
    private SimpleUserLogin simpleUserLogin;
    @Autowired
    private ResetPasswordTokenManager resetPasswordTokenManager;

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenChangePasswordDto_whenCalled_shouldChangePassword() {
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

        ChangePasswordDto changePasswordDto = new ChangePasswordDto();
        changePasswordDto.password = "passwords";
        changePasswordDto.passwordConfirmation = "passwords";
        changePasswordDto.emailConfirmation = "testing1@gmail.com";
        changePasswordDto.currentPassword = "password";

        restTemplate.exchange("/authentication/password", HttpMethod.PUT,
            new HttpEntity<>(changePasswordDto),
            String.class);

        InternalUserPassword actual =
            simpleUserLogin.login("testing1@gmail.com", "passwords");
        assertNotNull(actual.getPassword());
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql"})
    public void givenForgotPasswordDto_whenForgotCalled_shouldCreateResetPassword() {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.email = "testing1@gmail.com";

        restTemplate.exchange("/authentication/password/forgot",
            HttpMethod.POST,
            new HttpEntity<>(forgotPasswordDto),
            String.class);

        InternalResetPasswordToken resetPasswordTokenModel =
            resetPasswordTokenManager.findByUserEmail("testing1@gmail.com");

        assertNotNull(resetPasswordTokenModel);
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenResetPasswordExists_whenForgotCalled_shouldReturn200() {
        ForgotPasswordDto forgotPasswordDto = new ForgotPasswordDto();
        forgotPasswordDto.email = "testing1@gmail.com";

        restTemplate.exchange("/authentication/password/forgot",
            HttpMethod.POST,
            new HttpEntity<>(forgotPasswordDto),
            String.class);

        ResponseEntity<String> response =
            restTemplate.exchange("/authentication/password/forgot",
                HttpMethod.POST,
                new HttpEntity<>(forgotPasswordDto),
                String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenResetPasswordTokenDto_whenResetCalled_shouldUpdatePassword() {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel userModel = new UserModel();
//        userModel.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(userModel);
//        userPasswordModel.setPassword("password");
//
//        userPasswordManager.create(userPasswordModel);
//
//        InternalResetPasswordToken resetPasswordTokenModel =
//            new InternalResetPasswordToken();
//        resetPasswordTokenModel.setUser(userModel);

//        resetPasswordTokenModel =
//            resetPasswordTokenManager.create(resetPasswordTokenModel);
//
//        ResetPasswordTokenDto resetPasswordTokenDto =
//            new ResetPasswordTokenDto();
//        resetPasswordTokenDto.token = resetPasswordTokenModel.getToken();
//        resetPasswordTokenDto.password = "passwords";
//        resetPasswordTokenDto.passwordConfirmation = "passwords";

//        restTemplate.exchange("/authentication/password/reset", HttpMethod.POST,
//            new HttpEntity<>(resetPasswordTokenDto),
//            String.class);

        InternalUserPassword actual =
            simpleUserLogin.login("testing1@gmail.com", "passwords");
        assertNotNull(actual.getPassword());
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenNoUserPassword_whenResetCalled_shouldCreatePassword() {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel userModel = new UserModel();
//        userModel.setId((Integer) userList.get(0).get("id"));
//
//        InternalResetPasswordToken resetPasswordTokenModel =
//            new InternalResetPasswordToken();
//        resetPasswordTokenModel.setUser(userModel);

//        resetPasswordTokenModel =
//            resetPasswordTokenManager.create(resetPasswordTokenModel);
//
//        ResetPasswordTokenDto resetPasswordTokenDto =
//            new ResetPasswordTokenDto();
//        resetPasswordTokenDto.token = resetPasswordTokenModel.getToken();
//        resetPasswordTokenDto.password = "passwords";
//        resetPasswordTokenDto.passwordConfirmation = "passwords";
//
//        restTemplate.exchange("/authentication/password/reset", HttpMethod.POST,
//            new HttpEntity<>(resetPasswordTokenDto),
//            String.class);

        InternalUserPassword actual =
            simpleUserLogin.login("testing1@gmail.com", "passwords");
        assertNotNull(actual.getPassword());
    }
}
