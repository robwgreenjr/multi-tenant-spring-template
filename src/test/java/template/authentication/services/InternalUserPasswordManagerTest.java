package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.UserPasswordCreateIncompleteException;
import template.authentication.exceptions.UserPasswordNotFoundException;
import template.authentication.exceptions.UserPasswordUpdateIncompleteException;
import template.authentication.models.InternalUserPassword;
import template.helpers.IntegrationTest;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class InternalUserPasswordManagerTest extends IntegrationTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private UserPasswordManager userPasswordManager;

    @Test
    public void givenUserPasswordExists_whenFindByEmailIsCalled_shouldReturnUserPassword() {
//        InternalUserPassword actual =
//            userPasswordManager.findByUserEmail("testing1@gmail.com");
//
//        assertEquals("testing1@gmail.com", actual.getUser().getEmail());
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNoUserPassword_whenFindByEmailIsCalled_shouldThrowException() {
        userPasswordManager.findByUserEmail("testing@gmail.com");
    }

    @Test
    public void givenNewUserPassword_whenCreate_shouldStoreNewUserPassword() {
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

        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password JOIN internal.user us on us.id = internal.authentication_user_password.user_id WHERE us.email = 'testing1@gmail.com'");

        assertEquals(1, userPasswordList.size());
    }

    @Test
    public void givenNewUserPassword_whenCreate_shouldSetCreatedOnField() {
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(user);
//        userPasswordModel.setPassword("password");
//
//        userPasswordModel = userPasswordManager.create(userPasswordModel);
//
//        List<Map<String, Object>> userPasswordList =
//            jdbcTemplate.queryForList(
//                "SELECT * FROM internal.authentication_user_password WHERE password = '" +
//                    userPasswordModel.getPassword() + "';");

//        assertNotNull(userPasswordList.get(0).get("created_on"));
    }

    @Test(expected = UserPasswordCreateIncompleteException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenNewUserPasswordWithoutUser_whenCreate_shouldThrowException() {
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        userPasswordModel.setPassword("password");

        userPasswordManager.create(userPasswordModel);
    }

    @Test
    public void givenUpdatePassword_whenUpdate_shouldUpdateUserPassword()
        throws Exception {
        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password'");
        List<Map<String, Object>> userList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.user WHERE email = 'testing1@gmail.com'");
//        UserModel user = new UserModel();
//        user.setId((Integer) userList.get(0).get("id"));
//        InternalUserPassword userPasswordModel = new InternalUserPassword();
//        userPasswordModel.setUser(user);
//        userPasswordModel.setPassword("password updated");

//        Timestamp createdOn =
//            (Timestamp) userPasswordList.get(0).get("created_on");
//        userPasswordModel.setCreatedOn(createdOn.toInstant());
//
//        userPasswordManager.update((Integer) userPasswordList.get(0).get("id"),
//            userPasswordModel);

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password updated'");

        assertEquals("password updated", actualList.get(0).get("password"));
    }

    @Test(expected = UserPasswordUpdateIncompleteException.class)
    public void givenUpdatePasswordWithoutUser_whenUpdate_shouldThrowException()
        throws Exception {
        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password'");
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        userPasswordModel.setPassword("password updated");

        userPasswordManager.update((Integer) userPasswordList.get(0).get("id"),
            userPasswordModel);
    }

    @Test
    public void givenUpdatePassword_whenupdatePartial_shouldUpdatePassword() {
        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password'");
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        userPasswordModel.setPassword("password updated");

        userPasswordManager.updatePartial(
            (Integer) userPasswordList.get(0).get("id"),
            userPasswordModel);

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password updated'");

        assertEquals("password updated", actualList.get(0).get("password"));
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenUpdatePasswordWithNoneExistingUser_whenUpdatePartial_shouldThrowException() {
        InternalUserPassword userPasswordModel = new InternalUserPassword();
        userPasswordModel.setPassword("password updated");

        userPasswordManager.updatePartial(1, userPasswordModel);
    }

    @Test
    public void givenDeletePassword_whenDelete_shouldRemoveUserPassword() {
        List<Map<String, Object>> userPasswordList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password'");

        userPasswordManager.delete((Integer) userPasswordList.get(0).get("id"));

        List<Map<String, Object>> actualList =
            jdbcTemplate.queryForList(
                "SELECT * FROM internal.authentication_user_password WHERE password = 'password'");

        assertEquals(0, actualList.size());
    }

    @Test(expected = UserPasswordNotFoundException.class)
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenDeletePasswordWithNoneExistingUser_whenDelete_shouldThrowException() {
        userPasswordManager.delete(1);
    }
}
