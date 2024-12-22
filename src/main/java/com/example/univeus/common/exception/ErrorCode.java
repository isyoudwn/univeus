package com.example.univeus.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {
    /**
     * auth
     */
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AUTH-001", "만료된 refresh token 입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-002", "refresh token이 존재하지 않습니다."),
    ACCESS_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-002", "access token이 존재하지 않습니다."),
    AUTH_BAD_REQUEST(HttpStatus.BAD_REQUEST, "AUTH-003", "접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String errorMessage;
}
