package com.example.univeus.domain.meeting.model;


import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.meeting.exception.MeetingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingCategory {
    STUDY("스터디모집"),

    MEETING("모임"),
    ETC("기타");

    private final String category;

    public static MeetingCategory of(String value) {
        for (MeetingCategory meetingCategory : MeetingCategory.values()) {
            if (meetingCategory.category.equals(value)) {
                return meetingCategory;
            }
        }
        throw new MeetingException(INVALID_MEETING_CATEGORY);
    }
}
