package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UserPasswordNotFoundException extends ResponseStatusException {
    public UserPasswordNotFoundException() {
        super(HttpStatus.NOT_FOUND, "User password wasn't found.");
    }
}
