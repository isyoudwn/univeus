package com.example.univeus.domain.meeting.model;

import static com.example.univeus.common.response.ResponseMessage.*;

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
public class PostDeadline {
    private LocalDateTime postDeadline;

    public static PostDeadline of(LocalDateTime postDeadline, LocalDateTime now) {
        if (postDeadline.isBefore(now)) {
            throw new MeetingException(POST_DEADLINE_IS_PAST);
        }
        return new PostDeadline(postDeadline);
    }
}
