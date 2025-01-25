package com.example.univeus.domain.meeting.model;


import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.meeting.exception.MeetingException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingCategory {
    STUDY("스터디모집"),
    MEETING("모임"),
    EXERCISE("운동"),
    ETC("기타"),
    CULTURE("문화활동"),
    TRAVEL("여행"),
    HOBBY("취미"),
    VOLUNTEER("봉사활동"),
    CAREER("커리어/취업"),
    ART("예술"),
    FOOD("음식"),
    PET("반려동물"),
    TECH("기술"),
    MUSIC("음악"),
    ENVIRONMENT("환경"),
    GAMING("게임"),
    DATING("데이트"),
    LANGUAGE("언어교환"),
    SCIENCE("과학"),
    LITERATURE("문학"),
    DESIGN("디자인");

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
