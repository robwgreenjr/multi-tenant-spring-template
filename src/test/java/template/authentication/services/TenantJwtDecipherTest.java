package template.authentication.services;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.helpers.JwtSpecialist;
import template.helpers.IntegrationTest;
import template.tenants.models.TenantUser;

import static org.junit.Assert.assertNotNull;

public class TenantJwtDecipherTest extends IntegrationTest {

    @Autowired
    @Qualifier("TenantJwtDecipher")
    private AuthenticationProcessor jwtDecipher;
    @Autowired
    @Qualifier("TenantJwtSpecialist")
    private JwtSpecialist<TenantUser> simpleJwtSpecialist;

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenToken_whenValidate_shouldSetUserId() {
        TenantUser user = new TenantUser();
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