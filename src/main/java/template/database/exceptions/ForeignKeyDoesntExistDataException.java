package template.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ForeignKeyDoesntExistDataException extends ResponseStatusException {
    public ForeignKeyDoesntExistDataException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
