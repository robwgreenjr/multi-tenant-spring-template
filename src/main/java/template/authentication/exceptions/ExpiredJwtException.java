package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ExpiredJwtException extends ResponseStatusException {
    public ExpiredJwtException() {
        super(HttpStatus.UNAUTHORIZED, "Your session has expired.");
    }
}