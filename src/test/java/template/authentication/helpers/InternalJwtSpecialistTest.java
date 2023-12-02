package template.authentication.helpers;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.InvalidJwtException;
import template.helpers.IntegrationTest;

public class InternalJwtSpecialistTest extends IntegrationTest {
    @Autowired
    private InternalJwtSpecialist simpleJwtSpecialist;

    @Test
    @Sql(scripts = {"classpath:sql/global/configuration/create.sql"})
    public void givenUserAndScopeList_whenGenerate_shouldReturnToken() {
//        UserModel userModel = new UserModel();
//        userModel.setEmail("testing@gmail.com");
//
//        String token = simpleJwtSpecialist.generate(userModel, "scopeList");

//        Assertions.assertNotNull(token);
    }

    @Test
    @Sql(scripts = {"classpath:sql/global/configuration/create.sql"})
    public void givenToken_whenValidate_shouldReturnClaim() {
//        UserModel userModel = new UserModel();
//        userModel.setEmail("testing@gmail.com");
//
//        String token = simpleJwtSpecialist.generate(userModel, "scopeList");

//        Claims claims = simpleJwtSpecialist.validate(token);
//        Assertions.assertNotNull(claims);
    }

    @Test(expected = InvalidJwtException.class)
    @Sql(scripts = {"classpath:sql/global/configuration/create.sql"})
    public void givenInvalidToken_whenValidate_shouldReturnClaim() {
//        UserModel userModel = new UserModel();
//        userModel.setEmail("testing@gmail.com");
//
//        String token = simpleJwtSpecialist.generate(userModel, "scopeList");
//        token += "invalid";
//
//        simpleJwtSpecialist.validate(token);
    }

    @Test(expected = InvalidJwtException.class)
    @Sql(scripts = {"classpath:sql/global/configuration/create.sql"})
    public void givenInvalidTokenType_whenValidate_shouldReturnClaim() {
//        UserModel userModel = new UserModel();
//        userModel.setEmail("testing@gmail.com");
//
//        String token = simpleJwtSpecialist.generate(userModel, "scopeList");
//        String[] temp = token.split("\\.");
//        temp[1] = "invalid" + temp[1];
//        token = String.join("", temp);
//
//        simpleJwtSpecialist.validate(token);
    }
}