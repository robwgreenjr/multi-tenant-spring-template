package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordNotSetException extends ResponseStatusException {
    public PasswordNotSetException() {
        super(HttpStatus.BAD_REQUEST, "Your password hasn't been set yet.");
    }
}