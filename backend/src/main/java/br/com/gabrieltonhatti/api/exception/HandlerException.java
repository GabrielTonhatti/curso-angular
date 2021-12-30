package br.com.gabrieltonhatti.api.exception;

import br.com.gabrieltonhatti.api.model.ApiException;
import javassist.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class HandlerException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiException> handleNotFoundException(NotFoundException notFoundException) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(createResponse(notFoundException.getMessage(), HttpStatus.NOT_FOUND.value()));
    }

    private ApiException createResponse(String message, int httpValue) {
        return new ApiException(message, httpValue);
    }

}
