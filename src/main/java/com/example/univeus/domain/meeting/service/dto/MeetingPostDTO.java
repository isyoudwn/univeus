package com.example.univeus.domain.meeting.service.dto;

import com.example.univeus.domain.meeting.model.Coordinate;
import com.example.univeus.domain.meeting.model.Location;
import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingSchedule;
import com.example.univeus.domain.meeting.model.PostDeadline;
import com.example.univeus.domain.member.model.Gender;

public class MeetingPostDTO {

    public record MeetingPostDetailDTO(
            String title,
            String body,
            Gender genderLimit,
            Integer joinLimit,
            MeetingCategory meetingCategory,
            PostDeadline postDeadline,
            MeetingSchedule meetingSchedule,
            Coordinate coordinate,
            Location location
    ) {
    }
}
