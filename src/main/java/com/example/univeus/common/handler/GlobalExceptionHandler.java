package com.example.univeus.common.handler;


import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.exception.AuthException;
import com.example.univeus.domain.member.exception.MemberException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthException.class)
    public ResponseEntity<Response<String>> handleAuthException(AuthException ex) {
        return ResponseEntity
                .badRequest()
                .body(Response.failure(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<Response<String>> handleMemberException(MemberException ex) {
        return ResponseEntity
                .badRequest()
                .body(Response.failure(ex.getCode(), ex.getMessage()));
    }
}
