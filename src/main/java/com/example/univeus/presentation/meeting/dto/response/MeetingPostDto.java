package com.example.univeus.presentation.meeting.dto.response;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPostStatus;
import com.example.univeus.presentation.member.dto.request.MemberDto;
import java.time.LocalDateTime;
import java.util.List;

public class MeetingPostDto {
    public record MainPageResponse(
            List<MainPage> mainPage,
            String cursor,
            Boolean hasNext
    ) {
    }

    public record MainPage(
            String title,
            Integer joinLimit,
//            Integer nowParticipantsCount,
            MeetingPostStatus meetingPostStatus,
            String genderLimit,
            LocalDateTime postDeadline,
            LocalDateTime meetingSchedule,
            MeetingCategory meetingCategory,
            MemberDto.Profile writer
    ) {
    }
}
