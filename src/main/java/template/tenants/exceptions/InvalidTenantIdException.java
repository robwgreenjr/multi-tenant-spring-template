package template.tenants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidTenantIdException extends ResponseStatusException {
    public InvalidTenantIdException() {
        super(HttpStatus.BAD_REQUEST, "Invalid tenant id provided.");
    }
}