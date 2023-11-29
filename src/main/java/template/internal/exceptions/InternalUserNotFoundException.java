package template.internal.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InternalUserNotFoundException extends ResponseStatusException {
    public InternalUserNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User not found.");
    }
}