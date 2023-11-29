package template.authorization.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotAuthorizedException extends ResponseStatusException {
    public NotAuthorizedException(String scope) {
        super(HttpStatus.FORBIDDEN,
                "Your not authorized to view (" + scope + ") requested resources.");
    }
}