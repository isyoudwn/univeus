package com.example.univeus.domain.scheduler.exception;

import com.example.univeus.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class SchedulingException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public SchedulingException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
