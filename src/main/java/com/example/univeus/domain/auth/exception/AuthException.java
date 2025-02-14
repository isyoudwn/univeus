package com.example.univeus.domain.auth.exception;

import com.example.univeus.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class AuthException extends RuntimeException {

    private final ResponseMessage responseMessage;

    public AuthException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
