package template.tenants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TenantEmailConfirmationNotFoundException
    extends ResponseStatusException {
    public TenantEmailConfirmationNotFoundException() {
        super(HttpStatus.BAD_REQUEST,
            "Your email confirmation token is incorrect.");
    }
}