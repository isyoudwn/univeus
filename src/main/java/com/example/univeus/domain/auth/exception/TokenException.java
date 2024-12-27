package com.example.univeus.domain.auth.exception;

import com.example.univeus.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class TokenException extends AuthException {
    private final ResponseMessage responseMessage;

    public TokenException(ResponseMessage responseMessage) {
        super(responseMessage);
        this.responseMessage = responseMessage;
    }
}
