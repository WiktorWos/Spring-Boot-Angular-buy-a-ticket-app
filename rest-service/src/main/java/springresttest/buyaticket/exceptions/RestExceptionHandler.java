package springresttest.buyaticket.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class RestExceptionHandler {
    @ExceptionHandler(value = {UserNotFoundException.class, ConnectionNotFoundException.class})
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException e) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        ErrorResponse error =
                new ErrorResponse(HttpStatus.NOT_FOUND.value(), messages,  System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {WrongCredentialsException.class})
    public ResponseEntity<ErrorResponse> handleUnauthorised(RuntimeException e) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        ErrorResponse error =
                new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), messages,  System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {UsedEmailException.class, DuplicateConnectionException.class})
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException e) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        ErrorResponse error =
                new ErrorResponse(HttpStatus.CONFLICT.value(), messages,  System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.toList());
        ErrorResponse error =
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations()
                .stream()
                .map(x -> x.getConstraintDescriptor().getMessageTemplate())
                .collect(Collectors.toList());
        ErrorResponse error =
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        e.printStackTrace();
        ErrorResponse error =
                new ErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
