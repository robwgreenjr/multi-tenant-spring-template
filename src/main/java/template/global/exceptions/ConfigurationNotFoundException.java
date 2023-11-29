package template.global.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ConfigurationNotFoundException extends ResponseStatusException {
    public ConfigurationNotFoundException() {
        super(HttpStatus.NOT_FOUND, "Configuration wasn't found.");
    }
}