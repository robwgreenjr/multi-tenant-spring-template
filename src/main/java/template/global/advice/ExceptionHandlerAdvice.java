package template.global.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import template.database.helpers.DatabaseExceptionHandler;
import template.global.dtos.ErrorDto;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private final DatabaseExceptionHandler databaseExceptionHandler;

    public ExceptionHandlerAdvice(
        DatabaseExceptionHandler databaseExceptionHandler) {
        this.databaseExceptionHandler = databaseExceptionHandler;
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<ErrorDto> handleConflict(Exception exception) {
        var databaseException =
            databaseExceptionHandler.exceptionHandler(exception);
        if (databaseException.isPresent()) {
            return databaseException.get();
        }

        if (exception instanceof ResponseStatusException) {
            return new ResponseEntity<>(
                new ErrorDto(HttpStatus.valueOf(
                    ((ResponseStatusException) exception).getStatusCode()
                        .value()),
                    ((ResponseStatusException) exception).getReason()),
                HttpStatus.valueOf(
                    ((ResponseStatusException) exception).getStatusCode()
                        .value()));
        }

        return new ResponseEntity<>(
            new ErrorDto(HttpStatus.INTERNAL_SERVER_ERROR,
                "Oops, please contact support at contact.me@template.io"),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }
}