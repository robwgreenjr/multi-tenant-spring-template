package template.database.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class InvalidDateFormatException extends ResponseStatusException {
    public InvalidDateFormatException(String field) {
        super(HttpStatus.BAD_REQUEST, "Invalid Date format provided for '" +
            field +
            "'. Only support ISO 8601 formats.");
    }
}
