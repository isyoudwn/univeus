package com.example.univeus.domain.meeting.model;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MeetingPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member writer;

    @Column(length = 50)
    private String title;

    @Column(length = 1000)
    private String body;

    @Column
    private Integer joinLimit;

    @Enumerated(EnumType.STRING)
    private Gender genderLimit;

    @Embedded
    private Location location;

    @Embedded
    private PostDeadline postDeadLine;

    @Embedded
    private MeetingSchedule meetingSchedule;

    @Enumerated(EnumType.STRING)
    private MeetingCategory meetingCategory;

    public static MeetingPost create(Member writer, String title, String body,
                                     Integer joinLimit, Gender genderLimit,
                                     Location location, PostDeadline postDeadLine,
                                     MeetingSchedule meetingSchedule, MeetingCategory meetingCategory) {

        if (joinLimit > 20 || joinLimit <= 0) {
            throw new MeetingException(ResponseMessage.JOIN_LIMIT_INVALID);
        }

        return MeetingPost.builder()
                .writer(writer)
                .title(title)
                .body(body)
                .joinLimit(joinLimit)
                .genderLimit(genderLimit)
                .location(location)
                .postDeadLine(postDeadLine)
                .meetingSchedule(meetingSchedule)
                .meetingCategory(meetingCategory)
                .build();
    }
}
