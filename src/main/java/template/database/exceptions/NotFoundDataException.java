package template.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundDataException extends ResponseStatusException {
    public NotFoundDataException(String reason) {
        super(HttpStatus.NOT_FOUND, reason);
    }
}
