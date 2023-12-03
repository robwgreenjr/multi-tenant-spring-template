package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserPasswordInvalidException extends ResponseStatusException {
    public UserPasswordInvalidException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}