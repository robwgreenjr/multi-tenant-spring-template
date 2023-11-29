package template.database.helpers;

import template.global.dtos.ErrorDto;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface DatabaseExceptionHandler {
    Optional<ResponseEntity<ErrorDto>> exceptionHandler(Exception exception);
}