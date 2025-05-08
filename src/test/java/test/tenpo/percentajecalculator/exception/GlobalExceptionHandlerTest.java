package test.tenpo.percentajecalculator.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import test.tenpo.percentajecalculator.model.ErrorDetails;
import test.tenpo.percentajecalculator.model.ResponseWrapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleGlobalException_MethodArgumentTypeMismatchException() {
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);
        when(exception.getPropertyName()).thenReturn("fieldName");
        when(exception.getValue()).thenReturn("invalidValue");

        ResponseEntity<Object> response = globalExceptionHandler.handleMethodArgumentTypeMismatch(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ResponseWrapper responseBody = (ResponseWrapper) response.getBody();
        assertNotNull(responseBody);
        assertNotNull(responseBody.getErrors());
        assertEquals(1, responseBody.getErrors().size());

        ErrorDetails errorDetails = responseBody.getErrors().getFirst();
        assertEquals("fieldName", errorDetails.getField());
        assertEquals("invalidValue", errorDetails.getRejectedValue());
    }
}