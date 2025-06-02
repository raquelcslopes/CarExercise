package exercise_vik_CarExercise.exceptions;

import exercise_vik_CarExercise.entity.Error;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RentacarExceptionHandler {

    @ExceptionHandler(value = {
            AccountDoesNotExistException.class,
            VehicleDoesNotExists.class})
    public ResponseEntity<Error> handlerNotFoundException(Exception ex, HttpServletRequest Request) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Error.builder()
                .message(ex.getMessage())
                .method(Request.getMethod())
                .path(Request.getRequestURI())
                .build());
    }
}
