package template.database.helpers;

import template.global.dtos.ErrorDto;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceException;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.SQLException;
import java.util.Optional;


public class DatabaseExceptionHandlerTest {
    private final DatabaseExceptionHandler databaseExceptionHandler =
            new DefaultDatabaseExceptionHandler();

    @Test
    public void givenNotFound_whenCalled_shouldReturn404() {
        NoResultException noResultException = new NoResultException();
        Optional<ResponseEntity<ErrorDto>> responseStatus = databaseExceptionHandler.exceptionHandler(noResultException);

        Assertions.assertEquals(responseStatus.get().getStatusCode(), HttpStatus.NOT_FOUND);
    }

    @Test
    public void givenDuplicate_whenCalled_shouldReturn400() {
        ConstraintViolationException constraintViolationException =
                new ConstraintViolationException("",
                        new SQLException("ERROR: duplicate key value violates" +
                                " unique constraint \"user_base_phone_key\"\n" +
                                "  Detail: Key (phone)=(555-555-5555) already" +
                                " exists."), "");
        PersistenceException persistenceException =
                new PersistenceException(constraintViolationException);

        Optional<ResponseEntity<ErrorDto>> responseStatus = databaseExceptionHandler.exceptionHandler(persistenceException);

        Assertions.assertEquals(responseStatus.get().getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenUnknownDuplicate_whenCalled_shouldReturn400() {
        ConstraintViolationException constraintViolationException =
                new ConstraintViolationException("", new SQLException("duplicate"), "");
        PersistenceException persistenceException =
                new PersistenceException(constraintViolationException);

        Optional<ResponseEntity<ErrorDto>> responseStatus = databaseExceptionHandler.exceptionHandler(persistenceException);

        Assertions.assertEquals(responseStatus.get().getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenUnknownNotNull_whenCalled_shouldReturn400() {
        ConstraintViolationException constraintViolationException =
                new ConstraintViolationException("", new SQLException("not-null"), "");
        PersistenceException persistenceException =
                new PersistenceException(constraintViolationException);

        Optional<ResponseEntity<ErrorDto>> responseStatus = databaseExceptionHandler.exceptionHandler(persistenceException);

        Assertions.assertEquals(responseStatus.get().getStatusCode(), HttpStatus.BAD_REQUEST);
    }

    @Test
    public void givenNotNull_whenCalled_shouldThrow400() {
        ConstraintViolationException constraintViolationException =
                new ConstraintViolationException("", new SQLException("not-null"), "");
        PersistenceException persistenceException =
                new PersistenceException(constraintViolationException);


        Optional<ResponseEntity<ErrorDto>> responseStatus = databaseExceptionHandler.exceptionHandler(persistenceException);

        Assertions.assertEquals(responseStatus.get().getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}