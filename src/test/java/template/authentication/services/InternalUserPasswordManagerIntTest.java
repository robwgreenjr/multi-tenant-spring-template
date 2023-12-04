package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.UserPasswordInvalidException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.exceptions.UserPasswordUpdateIncompleteException;
import template.authentication.models.InternalUserPassword;
import template.database.cli.Seeder;
import template.helpers.IntegrationTest;
import template.internal.models.InternalUser;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

public class InternalUserPasswordManagerIntTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    @Qualifier("InternalUserPasswordManager")
    private UserPasswordManager<InternalUserPassword> userPasswordManager;
    @Autowired
    private Seeder seeder;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUserPasswordExists_whenFindByEmailIsCalled_shouldReturnUserPassword() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");
        Optional<InternalUserPassword> actual =
            userPasswordManager.findByUserEmail(
                objectList.get(0).get("email").toString());

        assertTrue(actual.isPresent());
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNoUserPassword_whenFindByEmailIsCalled_shouldThrowException() {
        userPasswordManager.findByUserEmail("testing@gmail.com");
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewUserPassword_whenCreate_shouldStoreNewUserPassword() {
        seeder.internalUser(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");
        InternalUser user = new InternalUser();
        user.setId(Integer.valueOf(objectList.get(0).get("id").toString()));
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);

        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password JOIN internal.user us on us.id = internal.authentication_user_password.user_id WHERE us.email = '" +
                    objectList.get(0).get("email") + "'");

        assertEquals(1, userPasswordList.size());
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewUserPassword_whenCreate_shouldSetCreatedOnField() {
        seeder.internalUser(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList("SELECT * FROM internal.user");
        InternalUser user = new InternalUser();
        user.setId((Integer) objectList.get(0).get("id"));
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setUser(user);
        userPassword.setPassword("password");

        userPassword = userPasswordManager.create(userPassword);

        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password as aup JOIN internal.user u on u.id = aup.user_id WHERE u.id = '" +
                    objectList.get(0).get("id") + "';");

        assertNotNull(userPasswordList.get(0).get("created_on"));
    }

    @Test(expected = UserPasswordInvalidException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewUserPasswordWithoutUser_whenCreate_shouldThrowException() {
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setPassword("password");

        userPasswordManager.create(userPassword);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUpdatePassword_whenUpdate_shouldUpdateUserPassword() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user");
        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password");
        InternalUser user = new InternalUser();
        user.setId((Integer) userList.get(0).get("id"));
        InternalUserPassword userPassword = new InternalUserPassword();
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
                "SELECT * FROM internal.authentication_user_password");

        assertEquals("password updated", actualList.get(0).get("password"));
    }

    @Test(expected = UserPasswordUpdateIncompleteException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUpdatePasswordWithoutUser_whenUpdate_shouldThrowException() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password");
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setPassword("password updated");

        userPasswordManager.update((Integer) objectList.get(0).get("id"),
            userPassword);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUpdatePassword_whenUpdatePartial_shouldUpdatePassword() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password");
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setPassword("password updated");

        userPasswordManager.updatePartial(
            Integer.valueOf(objectList.get(0).get("id").toString()),
            userPassword);

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password updated'");

        assertEquals("password updated", actualList.get(0).get("password"));
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUpdatePasswordWithNoneExistingUser_whenUpdatePartial_shouldThrowException() {
        InternalUserPassword userPassword = new InternalUserPassword();
        userPassword.setPassword("password updated");

        userPasswordManager.updatePartial(1, userPassword);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenDeletePassword_whenDelete_shouldRemoveUserPassword() {
        seeder.internalUserPassword(jdbcTemplate, 1);
        List<Map<String, Object>> objectList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password");

        userPasswordManager.delete(
            Integer.valueOf(objectList.get(0).get("id").toString()));

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password");

        assertEquals(0, actualList.size());
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenDeletePasswordWithNoneExistingUser_whenDelete_shouldThrowException() {
        userPasswordManager.delete(1);
    }
}
