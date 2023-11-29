package template.tenants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TenantUserNotFoundException extends ResponseStatusException {
    public TenantUserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found.");
    }
}