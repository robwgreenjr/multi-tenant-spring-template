package template.authorization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PermissionNotFoundException extends ResponseStatusException {
    public PermissionNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Permission not found.");
    }
}