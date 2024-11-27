package ru.gb.task.manager.exceptions;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.List;

@SpringBootTest(classes = GlobalExceptionHandler.class)
public class GlobalExceptionHandlerTest {
    @Autowired
    private GlobalExceptionHandler globalExceptionHandler;

    @Test
    public void catchUserNotFoundExceptionTest() {
        ResponseEntity<AppError> responseEntity = globalExceptionHandler
                .catchUserNotFoundException(new UserNotFoundException("message"));
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 404);
        Assertions.assertEquals(responseEntity.getBody().getStatusCode(), 404);
        Assertions.assertEquals(responseEntity.getBody().getMessage(), "message");
    }

    @Test
    public void catchResourceNotFoundExceptionTest() {
        ResponseEntity<AppError> responseEntity = globalExceptionHandler
                .catchResourceNotFoundException(new ResourceNotFoundException("message"));
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 404);
        Assertions.assertEquals(responseEntity.getBody().getStatusCode(), 404);
        Assertions.assertEquals(responseEntity.getBody().getMessage(), "message");
    }

    @Test
    public void catchValidationExceptionTest() {
        ResponseEntity<FieldsValidationError> responseEntity = globalExceptionHandler
                .catchValidationException(new ValidationException(List.of("message1", "message2")));
        Assertions.assertEquals(responseEntity.getStatusCode().value(), 400);
        Assertions.assertEquals(responseEntity.getBody().getStatusCode(), 400);
        Assertions.assertEquals(responseEntity.getBody().getErrorFieldsMessages().size(), 2);
    }
}
