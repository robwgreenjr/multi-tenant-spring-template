package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidJwtException extends ResponseStatusException {
    public InvalidJwtException() {
        super(HttpStatus.BAD_REQUEST, "Invalid token provided.");
    }
}