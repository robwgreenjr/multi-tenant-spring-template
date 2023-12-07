package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.UserPasswordInvalidException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.exceptions.UserPasswordUpdateIncompleteException;
import template.authentication.models.TenantUserPassword;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;
import template.tenants.models.TenantUser;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class TenantUserPasswordManagerIntTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("TenantUserPasswordManager")
    private UserPasswordManager<TenantUserPassword> userPasswordManager;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUserPasswordExists_whenFindByEmailIsCalled_shouldReturnUserPassword() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");
        Optional<TenantUserPassword> actual =
            userPasswordManager.findByUserEmail(
                objectList.get(0).get("email").toString());

        assertTrue(actual.isPresent());
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNoUserPassword_whenFindByEmailIsCalled_shouldThrowException() {
        userPasswordManager.findByUserEmail("testing@gmail.com");
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNewUserPassword_whenCreate_shouldStoreNewUserPassword() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");
        TenantUser user = new TenantUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);

        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password JOIN tenant.user us on us.id = tenant.authentication_user_password.user_id WHERE us.email = '" +
                    objectList.get(0).get("email") + "'");

        assertEquals(1, userPasswordList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNewUserPassword_whenCreate_shouldSetCreatedOnField() {
        seeder.tenantUser(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM tenant.user");
        TenantUser user = new TenantUser();
        user.setId((Integer) objectList.get(0).get("id"));
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPassword = userPasswordManager.create(userPassword);

        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password as aup JOIN tenant.user u on u.id = aup.user_id WHERE u.id = '" +
                    objectList.get(0).get("id") + "';");

        assertNotNull(userPasswordList.get(0).get("created_on"));
    }

    @Test(expected = UserPasswordInvalidException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenNewUserPasswordWithoutUser_whenCreate_shouldThrowException() {
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUpdatePassword_whenUpdate_shouldUpdateUserPassword() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.user");
        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password");
        TenantUser user = new TenantUser();
        user.setId((Integer) userList.get(0).get("id"));
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password updated");

        Timestamp createdOn =
            (Timestamp) userPasswordList.get(0).get("created_on");
        userPassword.setCreatedOn(createdOn.toInstant());

        userPasswordManager.update(
            Integer.valueOf(userPasswordList.get(0).get("id").toString()),
            userPassword);

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password");

        assertEquals("password updated", actualList.get(0).get("password"));
    }

    @Test(expected = UserPasswordUpdateIncompleteException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUpdatePasswordWithoutUser_whenUpdate_shouldThrowException() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password");
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setPassword("password updated");

        userPasswordManager.update((Integer) objectList.get(0).get("id"),
            userPassword);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUpdatePassword_whenUpdatePartial_shouldUpdatePassword() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password");
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setPassword("password updated");

        userPasswordManager.updatePartial(
            Integer.valueOf(objectList.get(0).get("id").toString()),
            userPassword);

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password WHERE password = 'password updated'");

        assertEquals("password updated", actualList.get(0).get("password"));
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUpdatePasswordWithNoneExistingUser_whenUpdatePartial_shouldThrowException() {
        TenantUserPassword userPassword = new TenantUserPassword();
        userPassword.setPassword("password updated");

        userPasswordManager.updatePartial(1, userPassword);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenDeletePassword_whenDelete_shouldRemoveUserPassword() {
        seeder.tenantUserPassword(jdbcTemplate, tenantId, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password");

        userPasswordManager.delete(
            Integer.valueOf(objectList.get(0).get("id").toString()));

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM tenant.authentication_user_password");

        assertEquals(0, actualList.size());
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenDeletePasswordWithNoneExistingUser_whenDelete_shouldThrowException() {
        userPasswordManager.delete(1);
    }
}
