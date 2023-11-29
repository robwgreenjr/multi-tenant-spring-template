package template.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotNullColumnDataException extends ResponseStatusException {
    public NotNullColumnDataException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
