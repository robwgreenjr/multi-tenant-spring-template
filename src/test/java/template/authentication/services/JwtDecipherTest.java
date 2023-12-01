package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import template.authentication.helpers.SimpleJwtSpecialist;
import template.helpers.IntegrationTest;

public class JwtDecipherTest extends IntegrationTest {

    @Autowired
    private AuthenticationProcessor jwtDecipher;
    @Autowired
    private SimpleJwtSpecialist simpleJwtSpecialist;

    @Test
    public void givenToken_whenValidate_shouldSetUserId() {
//        UserModel userModel = new UserModel();
//        userModel.setId(1);
//        userModel.setEmail("testing1@gmail.com");
//
//        String token = simpleJwtSpecialist.generate(userModel, "scopeList");

//        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
//        httpServletRequest.addHeader("Authorization", "Bearer " + token);

//        jwtDecipher.validate(httpServletRequest);
//
//        assertNotNull(httpServletRequest.getAttribute("user_id"));
    }
}