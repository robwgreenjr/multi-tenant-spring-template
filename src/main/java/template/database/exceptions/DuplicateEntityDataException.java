package template.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DuplicateEntityDataException extends ResponseStatusException {
    public DuplicateEntityDataException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
