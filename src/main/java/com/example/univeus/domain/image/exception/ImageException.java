package com.example.univeus.domain.image.exception;

import com.example.univeus.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class ImageException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public ImageException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
