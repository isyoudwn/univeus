package com.example.univeus.common.handler;


import com.example.univeus.common.response.ErrorResponse;
import com.example.univeus.domain.auth.exception.AuthException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                ex.getHttpStatus().value(),
                ex.getCode(),
                ex.getMessage()
        );
        return new ResponseEntity<>(errorResponse, ex.getHttpStatus());
    }
}
