package template.tenants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTenantEmailException extends ResponseStatusException {
    public InvalidTenantEmailException() {
        super(HttpStatus.BAD_REQUEST, "Invalid or no tenant email provided.");
    }
}