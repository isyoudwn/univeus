package com.example.univeus.presentation.meeting.dto.response;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPostStatus;
import com.example.univeus.presentation.member.dto.request.MemberDto;
import java.time.LocalDateTime;
import java.util.List;

public class MainPageResponse {

    public record MainPage(
            List<MainPageDetail> mainPage,
            String cursor,
            Boolean hasNext
    ) {
    }

    public record MainPageDetail(
            String title,
            Integer joinLimit,
            MeetingPostStatus meetingPostStatus,
            String genderLimit,
            LocalDateTime postDeadline,
            LocalDateTime meetingSchedule,
            MeetingCategory meetingCategory,
            MemberDto.Profile writer
    ) {
    }
}
