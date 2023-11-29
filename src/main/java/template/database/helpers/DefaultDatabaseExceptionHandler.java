package template.database.helpers;

import template.global.dtos.ErrorDto;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class DefaultDatabaseExceptionHandler
    implements DatabaseExceptionHandler {
    @Override
    public Optional<ResponseEntity<ErrorDto>> exceptionHandler(
        Exception exception) {
        if (exception instanceof NoResultException) {
            return noResultException((NoResultException) exception);
        }

        if (exception instanceof EntityNotFoundException) {
            return entityNotFoundException((EntityNotFoundException) exception);
        }

        if (exception instanceof PersistenceException) {
            return persistenceExceptionHandler(
                (PersistenceException) exception);
        }

        if (exception instanceof EmptyResultDataAccessException) {
            return emptyResultDataAccessException(
                (EmptyResultDataAccessException) exception);
        }

        if (exception instanceof DataIntegrityViolationException) {
            return dataIntegrityViolationException(
                (DataIntegrityViolationException) exception);
        }

        if (exception instanceof IllegalArgumentException) {
            return dataColumnDoesntExistException(
                (IllegalArgumentException) exception);
        }

        if (exception instanceof ResponseStatusException) {
            return Optional.of(
                new ResponseEntity<>(
                    new ErrorDto(
                        (HttpStatus) ((ResponseStatusException) exception).getStatusCode(),
                        ((ResponseStatusException) exception).getReason()),
                    ((ResponseStatusException) exception).getStatusCode()));
        }

        return Optional.empty();
    }

    private Optional<ResponseEntity<ErrorDto>> dataColumnDoesntExistException(
        IllegalArgumentException exception) {
        String invalidColumn =
            exception.getMessage().split("\\[")[1].split("\\]")[0];

        return Optional.of(
            new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST,
                "Property '" + invalidColumn +
                    "' doesn't exist on requested resource."),
                HttpStatus.BAD_REQUEST));
    }

    private Optional<ResponseEntity<ErrorDto>> persistenceExceptionHandler(
        PersistenceException persistenceException) {
        if (persistenceException.getCause() instanceof ConstraintViolationException constraintViolationException) {
            if (constraintViolationException.getCause().getMessage()
                .contains("duplicate")) {
                String duplicateColumn = "";
                try {
                    duplicateColumn =
                        constraintViolationException.getCause().getMessage()
                            .split("\\(")[1].split("\\)=")[0] + " ";
                } catch (Exception exception) {
                    // do nothing
                }

                return Optional.of(new ResponseEntity<>(
                    new ErrorDto(HttpStatus.BAD_REQUEST,
                        "Your " + duplicateColumn +
                            " value already exists."),
                    HttpStatus.BAD_REQUEST));
            }

            Optional<ResponseEntity<ErrorDto>> isNotNull =
                notNullErrorMessageBuilder(constraintViolationException);
            if (isNotNull.isPresent()) {
                return isNotNull;
            }

            if (constraintViolationException.getCause().getMessage()
                .contains("foreign key") &&
                constraintViolationException.getCause().getMessage()
                    .contains("insert")) {
                return Optional.of(new ResponseEntity<>(
                    new ErrorDto(HttpStatus.BAD_REQUEST,
                        "Provided relationship doesn't exist."),
                    HttpStatus.BAD_REQUEST));
            }
        }

        return Optional.empty();
    }

    private Optional<ResponseEntity<ErrorDto>> noResultException(
        NoResultException noResultException) {
        return Optional.of(
            new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND,
                noResultException.getMessage()),
                HttpStatus.NOT_FOUND));
    }

    private Optional<ResponseEntity<ErrorDto>> emptyResultDataAccessException(
        EmptyResultDataAccessException emptyResultDataAccessException) {
        return Optional.of(
            new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND,
                emptyResultDataAccessException.getMessage()),
                HttpStatus.NOT_FOUND));
    }

    private Optional<ResponseEntity<ErrorDto>> entityNotFoundException(
        EntityNotFoundException entityNotFoundException) {
        return Optional.of(
            new ResponseEntity<>(new ErrorDto(HttpStatus.NOT_FOUND,
                entityNotFoundException.getMessage()),
                HttpStatus.NOT_FOUND));
    }

    private Optional<ResponseEntity<ErrorDto>> dataIntegrityViolationException(
        DataIntegrityViolationException dataIntegrityViolationException) {
        if (dataIntegrityViolationException.getCause() instanceof ConstraintViolationException constraintViolationException) {
            Optional<ResponseEntity<ErrorDto>> isNotNull =
                notNullErrorMessageBuilder(constraintViolationException);
            if (isNotNull.isPresent()) {
                return isNotNull;
            }

            if (constraintViolationException.getCause().getMessage()
                .contains("unique constraint")) {
                return Optional.of(
                    new ResponseEntity<>(new ErrorDto(HttpStatus.BAD_REQUEST,
                        "Provided value already exists, it needs to be unique."),
                        HttpStatus.BAD_REQUEST));
            }
        }

        return Optional.empty();
    }

    private Optional<ResponseEntity<ErrorDto>> notNullErrorMessageBuilder(
        ConstraintViolationException constraintViolationException) {
        Optional<ResponseEntity<ErrorDto>> result = Optional.empty();

        if (constraintViolationException.getCause().getMessage()
            .contains("not-null")) {
            String notNullColumn = "all values.";
            try {
                notNullColumn = "a " +
                    constraintViolationException.getCause().getMessage()
                        .split("\"")[1] +
                    " value.";
            } catch (Exception exception) {
                // do nothing
            }

            result = Optional.of(new ResponseEntity<>(
                new ErrorDto(HttpStatus.BAD_REQUEST,
                    "You must provide " + notNullColumn),
                HttpStatus.BAD_REQUEST));
        }

        return result;
    }
}