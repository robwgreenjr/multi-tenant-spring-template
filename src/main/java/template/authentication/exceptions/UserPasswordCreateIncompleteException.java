package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserPasswordCreateIncompleteException extends ResponseStatusException {
    public UserPasswordCreateIncompleteException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}