package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.ResetPasswordTokenCreateIncompleteException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.models.InternalResetPasswordToken;
import template.database.cli.Seeder;
import template.helpers.InternalIntegrationTest;
import template.internal.models.InternalUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;


public class InternalResetPasswordTokenManagerTest extends
    InternalIntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ResetPasswordTokenManager<InternalResetPasswordToken>
        resetPasswordTokenManager;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenResetPasswordTokenExists_whenFindByEmailIsCalled_shouldReturnResetPasswordToken() {
        seeder.internalUserResetPasswordToken(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        Optional<InternalResetPasswordToken> actual =
            resetPasswordTokenManager.findByUserEmail(
                objectList.get(0).get("email").toString());

        assertTrue(actual.isPresent());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNoResetPasswordTokenExists_whenFindByEmailIsCalled_shouldThrowException() {
        resetPasswordTokenManager.findByUserEmail("testing@gmail.com");
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenResetPasswordTokenExists_whenFindByTokenIsCalled_shouldReturnResetPasswordToken() {
        seeder.internalUserResetPasswordToken(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token");

        Optional<InternalResetPasswordToken> actual =
            resetPasswordTokenManager.findByToken(
                UUID.fromString(objectList.get(0).get("token").toString()));

        assertTrue(actual.isPresent());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNoResetPasswordTokenExists_whenFindByTokenIsCalled_shouldThrowException() {
        UUID uuid = UUID.randomUUID();

        resetPasswordTokenManager.findByToken(uuid);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewResetPasswordToken_whenCreate_shouldStoreNewResetPasswordToken() {
        seeder.internalUser(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        InternalUser user = new InternalUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        user.setEmail(objectList.get(0).get("email").toString());
        InternalResetPasswordToken resetPasswordTokenModel =
            new InternalResetPasswordToken();
        resetPasswordTokenModel.setUser(user);
        resetPasswordTokenModel.setToken(UUID.randomUUID());

        resetPasswordTokenManager.create(resetPasswordTokenModel);

        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token JOIN internal.user us on us.id = internal.authentication_user_reset_password_token.user_id WHERE us.email = '" +
                    objectList.get(0).get("email").toString() + "'");

        assertEquals(1, resetPasswordTokenList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewResetPasswordToken_whenCreate_shouldSetCreatedOnField() {
        seeder.internalUser(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");

        InternalUser user = new InternalUser();
        user.setId((Integer) objectList.get(0).get("id"));
        user.setEmail(objectList.get(0).get("email").toString());
        InternalResetPasswordToken resetPasswordTokenModel =
            new InternalResetPasswordToken();
        resetPasswordTokenModel.setUser(user);
        resetPasswordTokenModel.setToken(UUID.randomUUID());

        resetPasswordTokenModel =
            resetPasswordTokenManager.create(resetPasswordTokenModel);

        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token WHERE token = '" +
                    resetPasswordTokenModel.getToken().toString() +
                    "';");

        assertNotNull(resetPasswordTokenList.get(0).get("created_on"));
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
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenDeleteResetPasswordToken_whenDelete_shouldRemoveResetPasswordToken() {
        seeder.internalUserResetPasswordToken(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_reset_password_token");

        resetPasswordTokenManager.delete(
            UUID.fromString(objectList.get(0).get("token").toString()));

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
