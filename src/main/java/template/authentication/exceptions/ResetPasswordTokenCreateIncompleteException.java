package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResetPasswordTokenCreateIncompleteException extends ResponseStatusException {
    public ResetPasswordTokenCreateIncompleteException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}