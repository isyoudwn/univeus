package com.example.univeus.common.handler;


import com.example.univeus.common.response.Response;
import com.example.univeus.domain.auth.exception.AuthException;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.member.exception.MemberException;
import io.jsonwebtoken.JwtException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
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

    @ExceptionHandler(MeetingException.class)
    public ResponseEntity<Response<String>> handleMeetingPostException(MeetingException ex) {
        return ResponseEntity
                .badRequest()
                .body(Response.failure(ex.getCode(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Response<String>> handleDtoException(MethodArgumentNotValidException ex) {
        // TODO: 리팩토링 해야함
        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst().map(FieldError::getDefaultMessage)
                .orElse("형식 예외가 발생하였습니다.");

        return ResponseEntity
                .badRequest()
                .body(Response.failure("FORMAT-001", errorMessage));
    }
}
