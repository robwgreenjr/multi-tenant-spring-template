package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import template.authentication.helpers.InternalJwtSpecialist;
import template.helpers.IntegrationTest;

public class InternalJwtDecipherTest extends IntegrationTest {

    @Autowired
    @Qualifier("InternalJwtDecipher")
    private AuthenticationProcessor jwtDecipher;
    @Autowired
    private InternalJwtSpecialist simpleJwtSpecialist;

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