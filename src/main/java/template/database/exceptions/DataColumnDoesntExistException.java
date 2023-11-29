package template.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class DataColumnDoesntExistException extends ResponseStatusException {
    public DataColumnDoesntExistException(String reason) {
        super(HttpStatus.BAD_REQUEST, reason);
    }
}
