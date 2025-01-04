package com.example.univeus.domain.meeting.exception;

import com.example.univeus.common.response.ResponseMessage;
import lombok.Getter;

@Getter
public class MeetingException extends RuntimeException {
    private final ResponseMessage responseMessage;

    public MeetingException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    public String getCode() {
        return responseMessage.getCode();
    }
}
