package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.helpers.JwtSpecialist;
import template.helpers.IntegrationTest;
import template.internal.models.InternalUser;

import static org.junit.Assert.assertNotNull;

public class InternalJwtDecipherTest extends IntegrationTest {

    @Autowired
    @Qualifier("InternalJwtDecipher")
    private AuthenticationProcessor jwtDecipher;
    @Autowired
    @Qualifier("InternalJwtSpecialist")
    private JwtSpecialist<InternalUser> simpleJwtSpecialist;

    @Test
    @Sql(scripts = {"classpath:sql/truncateInternalSchema.sql"})
    public void givenToken_whenValidate_shouldSetUserId() {
        InternalUser user = new InternalUser();
        user.setId(1);
        user.setEmail("testing1@gmail.com");

        String token = simpleJwtSpecialist.generate(user, "scopeList");

        MockHttpServletRequest httpServletRequest =
            new MockHttpServletRequest();
        httpServletRequest.addHeader("Authorization", "Bearer " + token);

        jwtDecipher.validate(httpServletRequest);

        assertNotNull(httpServletRequest.getAttribute("user_id"));
    }
}