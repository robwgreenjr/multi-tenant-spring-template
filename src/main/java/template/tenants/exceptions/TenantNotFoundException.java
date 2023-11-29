package template.tenants.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class TenantNotFoundException extends ResponseStatusException {
    public TenantNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Tenant not found.");
    }
}