package template.tenants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TenantRegistrationClosedException extends ResponseStatusException {
    public TenantRegistrationClosedException() {
        super(HttpStatus.BAD_REQUEST,
                "Our automated registration is temporarily closed. Please contact rob.green@template.io if your interested in creating an account.");
    }
}