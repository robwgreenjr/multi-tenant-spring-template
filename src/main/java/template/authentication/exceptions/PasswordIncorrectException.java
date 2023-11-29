package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class PasswordIncorrectException extends ResponseStatusException {
    public PasswordIncorrectException() {
        super(HttpStatus.UNAUTHORIZED, "Your password isn't correct.");
    }
}