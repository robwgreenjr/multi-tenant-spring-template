package template.authentication.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ResetPasswordTokenNotFoundException extends ResponseStatusException {
    public ResetPasswordTokenNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Reset password token wasn't found.");
    }
}
