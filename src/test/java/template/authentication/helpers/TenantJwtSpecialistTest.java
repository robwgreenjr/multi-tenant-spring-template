package template.authentication.helpers;

import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import template.authentication.exceptions.InvalidJwtException;
import template.helpers.IntegrationTest;
import template.tenants.models.TenantUser;

import static org.junit.Assert.assertNotNull;

public class TenantJwtSpecialistTest extends IntegrationTest {
    @Autowired
    private TenantJwtSpecialist simpleJwtSpecialist;

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenUserAndScopeList_whenGenerate_shouldReturnToken() {
        TenantUser user = new TenantUser();
        user.setEmail("testing@gmail.com");

        String token = simpleJwtSpecialist.generate(user, "scopeList");

        assertNotNull(token);
    }

    @Test
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenToken_whenValidate_shouldReturnClaim() {
        TenantUser user = new TenantUser();
        user.setEmail("testing@gmail.com");

        String token = simpleJwtSpecialist.generate(user, "scopeList");

        Claims claims = simpleJwtSpecialist.validate(token);
        assertNotNull(claims);
    }

    @Test(expected = InvalidJwtException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenInvalidToken_whenValidate_shouldReturnClaim() {
        TenantUser user = new TenantUser();
        user.setEmail("testing@gmail.com");

        String token = simpleJwtSpecialist.generate(user, "scopeList");
        token += "invalid";

        simpleJwtSpecialist.validate(token);
    }

    @Test(expected = InvalidJwtException.class)
    @Sql(scripts = {"classpath:sql/truncateTenantSchema.sql"})
    public void givenInvalidTokenType_whenValidate_shouldReturnClaim() {
        TenantUser user = new TenantUser();
        user.setEmail("testing@gmail.com");

        String token = simpleJwtSpecialist.generate(user, "scopeList");
        String[] temp = token.split("\\.");
        temp[1] = "invalid" + temp[1];
        token = String.join("", temp);

        simpleJwtSpecialist.validate(token);
    }
}