package com.example.univeus.domain.meeting.model;

import static com.example.univeus.common.response.ResponseMessage.MEETING_SCHEDULE_IS_AFTER_THAN_POST_DEADLINE;
import static com.example.univeus.common.response.ResponseMessage.MEETING_SCHEDULE_IS_PAST;

import com.example.univeus.domain.meeting.exception.MeetingException;
import jakarta.persistence.Embeddable;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetingSchedule {

    private LocalDateTime meetingSchedule;

    public static MeetingSchedule of(LocalDateTime meetingSchedule, LocalDateTime now, PostDeadline postDeadline) {
        if (meetingSchedule.isBefore(now)) {
            throw new MeetingException(MEETING_SCHEDULE_IS_PAST);
        }
        if (meetingSchedule.isBefore(postDeadline.getPostDeadline())) {
            throw new MeetingException(MEETING_SCHEDULE_IS_AFTER_THAN_POST_DEADLINE);
        }
        return new MeetingSchedule(meetingSchedule);
    }
}
