package template.authorization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RoleNotFoundException extends ResponseStatusException {
    public RoleNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Role not found.");
    }
}