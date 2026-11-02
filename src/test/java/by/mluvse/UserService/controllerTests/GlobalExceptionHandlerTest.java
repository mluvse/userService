package by.mluvse.UserService.controllerTests;

import by.mluvse.UserService.exceptions.DuplicateResourceException;
import by.mluvse.UserService.exceptions.GlobalExceptionHandler;
import by.mluvse.UserService.exceptions.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    void handleNotFound_returns404() {
        ResponseEntity<?> response = handler.notFound(new NotFoundException("Not found"));
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void handleDuplicate_returns409() {
        ResponseEntity<?> response = handler.duplicate(new DuplicateResourceException("Duplicate"));
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
    }

    @Test
    void handleIllegalArgument_returns400() {
        ResponseEntity<?> response = handler.badRequest(new IllegalArgumentException("Bad request"));
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}
