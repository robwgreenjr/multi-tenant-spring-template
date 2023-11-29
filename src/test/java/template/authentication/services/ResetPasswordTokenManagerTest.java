package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.ResetPasswordTokenCreateIncompleteException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.models.InternalResetPasswordToken;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;


public class ResetPasswordTokenManagerTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ResetPasswordTokenManager resetPasswordTokenManager;

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/authentication/reset_password_token/create.sql"})
    public void givenResetPasswordTokenExists_whenFindByEmailIsCalled_shouldReturnResetPasswordToken() {
        InternalResetPasswordToken actual =
            resetPasswordTokenManager.findByUserEmail("testing1@gmail.com");

        assertEquals("testing1@gmail.com", actual.getUser().getEmail());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {
        "classpath:sql/truncateInternalSchema.sql"})
    public void givenNoResetPasswordTokenExists_whenFindByEmailIsCalled_shouldThrowException() {
        resetPasswordTokenManager.findByUserEmail("testing@gmail.com");
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/authentication/reset_password_token/create.sql"})
    public void givenResetPasswordTokenExists_whenFindByTokenIsCalled_shouldReturnResetPasswordToken() {
        UUID uuid = UUID.fromString("ba346530-9a2b-4483-9c71-816255d9ff59");

        InternalResetPasswordToken actual =
            resetPasswordTokenManager.findByToken(uuid);

        assertEquals(uuid, actual.getToken());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNoResetPasswordTokenExists_whenFindByTokenIsCalled_shouldThrowException() {
        UUID uuid = UUID.randomUUID();

        resetPasswordTokenManager.findByToken(uuid);
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/authentication/reset_password_token/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenNewResetPasswordToken_whenCreate_shouldStoreNewResetPasswordToken() {
        UUID uuid = UUID.randomUUID();

        List<Map<String, Object>> userList = jdbcTemplate.queryForList(
            "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        user.setEmail("testing1@gmail.com");
//        InternalResetPasswordToken resetPasswordTokenModel =
//            new InternalResetPasswordToken();
//        resetPasswordTokenModel.setUser(user);
//        resetPasswordTokenModel.setToken(uuid);

//        resetPasswordTokenManager.create(resetPasswordTokenModel);

        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token JOIN internal.user us on us.id = internal.authentication_user_reset_password_token.user_id WHERE us.email = 'testing1@gmail.com'");

        assertEquals(1, resetPasswordTokenList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/authentication/reset_password_token/create.sql",
        "classpath:sql/global/configuration/create.sql"})
    public void givenNewResetPasswordToken_whenCreate_shouldSetCreatedOnField() {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        user.setEmail("testing1@gmail.com");
//        InternalResetPasswordToken resetPasswordTokenModel =
//            new InternalResetPasswordToken();
//        resetPasswordTokenModel.setUser(user);
//        resetPasswordTokenModel.setToken(UUID.randomUUID());
//
//        resetPasswordTokenModel =
//            resetPasswordTokenManager.create(resetPasswordTokenModel);

//        List<Map<String, Object>> resetPasswordTokenList =
//            jdbcTemplate.queryForList(
//                "SELECT * FROM internal.authentication_user_reset_password_token WHERE token = '" +
//                    resetPasswordTokenModel.getToken().toString() +
//                    "';");

//        assertNotNull(resetPasswordTokenList.get(0).get("created_on"));
    }

    @Test(expected = ResetPasswordTokenCreateIncompleteException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewResetPasswordTokenWithoutUser_whenCreate_shouldThrowException() {
        InternalResetPasswordToken resetPasswordTokenModel =
            new InternalResetPasswordToken();
        resetPasswordTokenModel.setToken(UUID.randomUUID());

        resetPasswordTokenManager.create(resetPasswordTokenModel);
    }

    @Test
    @Sql(scripts = {"classpath:sql/users/create.sql",
        "classpath:sql/authentication/reset_password_token/create.sql"})
    public void givenDeleteResetPasswordToken_whenDelete_shouldRemoveResetPasswordToken() {
        resetPasswordTokenManager.delete(
            UUID.fromString("ba346530-9a2b-4483-9c71-816255d9ff59"));

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token WHERE token = 'ba346530-9a2b-4483-9c71-816255d9ff59'");

        assertEquals(0, actualList.size());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenDeletePasswordWithNoneExistingUser_whenDelete_shouldThrowException() {
        resetPasswordTokenManager.delete(UUID.randomUUID());
    }
}
