package com.example.univeus.domain.auth.exception;

import com.example.univeus.common.exception.ErrorCode;
import lombok.Getter;

@Getter
public class TokenException extends AuthException {
    private final ErrorCode errorCode;

    public TokenException(ErrorCode errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
    }
}
