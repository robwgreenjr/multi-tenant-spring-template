package template.aws.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class EmailSenderException extends ResponseStatusException {
    public EmailSenderException() {
        super(HttpStatus.INTERNAL_SERVER_ERROR, "There was an error with our email service.");
    }
}