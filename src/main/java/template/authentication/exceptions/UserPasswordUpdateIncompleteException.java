package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserPasswordUpdateIncompleteException extends ResponseStatusException {
    public UserPasswordUpdateIncompleteException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}