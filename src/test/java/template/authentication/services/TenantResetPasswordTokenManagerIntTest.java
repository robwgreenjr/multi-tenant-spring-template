package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.ResetPasswordTokenCreateIncompleteException;
import template.authentication.exceptions.ResetPasswordTokenNotFoundException;
import template.authentication.models.TenantResetPasswordToken;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;
import template.tenants.models.TenantUser;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.*;


public class TenantResetPasswordTokenManagerIntTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("TenantResetPasswordTokenManager")
    private ResetPasswordTokenManager<TenantResetPasswordToken>
        resetPasswordTokenManager;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenResetPasswordTokenExists_whenFindByEmailIsCalled_shouldReturnResetPasswordToken() {
        seeder.tenantUserResetPasswordToken(jdbcTemplate, tenantId, 1);

        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        Optional<TenantResetPasswordToken> actual =
            resetPasswordTokenManager.findByUserEmail(
                objectList.get(0).get("email").toString());

        assertTrue(actual.isPresent());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNoResetPasswordTokenExists_whenFindByEmailIsCalled_shouldThrowException() {
        resetPasswordTokenManager.findByUserEmail("testing@gmail.com");
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenResetPasswordTokenExists_whenFindByTokenIsCalled_shouldReturnResetPasswordToken() {
        seeder.tenantUserResetPasswordToken(jdbcTemplate, tenantId, 1);

        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token");

        Optional<TenantResetPasswordToken> actual =
            resetPasswordTokenManager.findByToken(
                UUID.fromString(objectList.get(0).get("token").toString()));

        assertTrue(actual.isPresent());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNoResetPasswordTokenExists_whenFindByTokenIsCalled_shouldThrowException() {
        UUID uuid = UUID.randomUUID();

        resetPasswordTokenManager.findByToken(uuid);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNewResetPasswordToken_whenCreate_shouldStoreNewResetPasswordToken() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);

        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user;");

        TenantUser user = new TenantUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        user.setEmail(objectList.get(0).get("email").toString());
        TenantResetPasswordToken resetPasswordTokenModel =
            new TenantResetPasswordToken();
        resetPasswordTokenModel.setUser(user);
        resetPasswordTokenModel.setToken(UUID.randomUUID());

        resetPasswordTokenManager.create(resetPasswordTokenModel);

        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token " +
                    "JOIN tenant.user us on us.id = tenant.authentication_user_reset_password_token.user_id " +
                    "WHERE us.email = ?;",
                objectList.get(0).get("email").toString());

        assertEquals(1, resetPasswordTokenList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNewResetPasswordToken_whenCreate_shouldSetCreatedOnField() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);

        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");

        TenantUser user = new TenantUser();
        user.setId((Integer) objectList.get(0).get("id"));
        user.setEmail(objectList.get(0).get("email").toString());
        TenantResetPasswordToken resetPasswordTokenModel =
            new TenantResetPasswordToken();
        resetPasswordTokenModel.setUser(user);
        resetPasswordTokenModel.setToken(UUID.randomUUID());

        resetPasswordTokenModel =
            resetPasswordTokenManager.create(resetPasswordTokenModel);

        List<Map<String, Object>> resetPasswordTokenList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token WHERE token = ?",
                resetPasswordTokenModel.getToken());

        assertNotNull(resetPasswordTokenList.get(0).get("created_on"));
    }

    @Test(expected = ResetPasswordTokenCreateIncompleteException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNewResetPasswordTokenWithoutUser_whenCreate_shouldThrowException() {
        TenantResetPasswordToken resetPasswordTokenModel =
            new TenantResetPasswordToken();
        resetPasswordTokenModel.setToken(UUID.randomUUID());

        resetPasswordTokenManager.create(resetPasswordTokenModel);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenDeleteResetPasswordToken_whenDelete_shouldRemoveResetPasswordToken() {
        seeder.tenantUserResetPasswordToken(jdbcTemplate, tenantId, 1);

        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token");

        resetPasswordTokenManager.delete(
            UUID.fromString(objectList.get(0).get("token").toString()));

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_reset_password_token WHERE token = 'ba346530-9a2b-4483-9c71-816255d9ff59'");

        assertEquals(0, actualList.size());
    }

    @Test(expected = ResetPasswordTokenNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenDeletePasswordWithNoneExistingUser_whenDelete_shouldThrowException() {
        resetPasswordTokenManager.delete(UUID.randomUUID());
    }
}
