package com.example.univeus.domain.participant.exception;

import com.example.univeus.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class ParticipantException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public ParticipantException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
