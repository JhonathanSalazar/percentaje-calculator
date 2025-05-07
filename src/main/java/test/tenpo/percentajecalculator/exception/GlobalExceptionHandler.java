package test.tenpo.percentajecalculator.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import test.tenpo.percentajecalculator.model.ErrorDetails;
import test.tenpo.percentajecalculator.model.ResponseWrapper;

import java.util.Collections;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleGlobalException(
            MethodArgumentTypeMismatchException ex
    ) {
        String ERROR_CAUSE = "Must be a number";

        ErrorDetails errorDetails = ErrorDetails.builder()
                .message(ERROR_CAUSE)
                .rejectedValue(ex.getValue().toString())
                .field(ex.getPropertyName())
                .build();

        ResponseWrapper responseWrapper = ResponseWrapper.builder()
                .errors(Collections.singletonList(errorDetails))
                .build();

        return new ResponseEntity<>(responseWrapper, HttpStatus.BAD_REQUEST);
    }
}