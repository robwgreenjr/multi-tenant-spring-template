package template.tenants.models;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TenantModelTest {
    @Test
    public void givenNoSubdomain_whenSetSubdomainFromEmail_shouldSetSubdomainFromEmail() {
        Tenant actual = new Tenant();
        actual.setEmail("test@template.io");

        actual.setSubdomainFromEmail();

        Assertions.assertEquals("template", actual.getSubdomain());
    }

    @Test
    public void givenSubdomain_whenSetSubdomainFromEmail_shouldNotOverrideExistingSubdomain() {
        Tenant actual = new Tenant();
        actual.setEmail("test@template.io");
        actual.setSubdomain("testing");

        actual.setSubdomainFromEmail();

        Assertions.assertEquals("testing", actual.getSubdomain());
    }

    @Test
    public void givenNoEmail_whenCheckIfValidEmail_shouldReturnFalse() {
        Tenant model = new Tenant();

        boolean actual = model.checkIfValidEmail();

        Assertions.assertFalse(actual);
    }

    @Test
    public void givenInvalidEmail_whenCheckIfValidEmail_shouldReturnFalse() {
        Tenant model = new Tenant();
        model.setEmail("testing");
        boolean actual = model.checkIfValidEmail();

        Assertions.assertFalse(actual);
    }

    @Test
    public void givenValidEmail_whenCheckIfValidEmail_shouldReturnFalse() {
        Tenant model = new Tenant();
        model.setEmail("testing@gmail.com");
        boolean actual = model.checkIfValidEmail();

        Assertions.assertTrue(actual);
    }
}
