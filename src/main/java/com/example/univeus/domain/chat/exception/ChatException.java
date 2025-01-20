package com.example.univeus.domain.chat.exception;

import com.example.univeus.common.response.ResponseMessage;

public class ChatException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public ChatException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
