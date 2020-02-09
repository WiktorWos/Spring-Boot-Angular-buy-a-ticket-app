package springresttest.buyaticket.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class UserExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(UserNotFoundException e) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        UserErrorResponse error =
                new UserErrorResponse(HttpStatus.NOT_FOUND.value(), messages,  System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(MethodArgumentNotValidException e) {
        List<String> messages = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(x -> x.getDefaultMessage())
                .collect(Collectors.toList());
        UserErrorResponse error =
                new UserErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(ConstraintViolationException e) {
        List<String> messages = e.getConstraintViolations()
                .stream()
                .map(x -> x.getConstraintDescriptor().getMessageTemplate())
                .collect(Collectors.toList());
        UserErrorResponse error =
                new UserErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<UserErrorResponse> handleException(Exception e) {
        List<String> messages = new ArrayList<>();
        messages.add(e.getMessage());
        e.printStackTrace();
        UserErrorResponse error =
                new UserErrorResponse(HttpStatus.BAD_REQUEST.value(), messages, System.currentTimeMillis());
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
