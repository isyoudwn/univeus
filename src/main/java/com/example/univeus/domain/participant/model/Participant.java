package com.example.univeus.domain.participant.model;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.member.model.Member;
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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Participant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    private Member member;

    @ManyToOne
    private MeetingPost meetingPost;

    @Enumerated(EnumType.STRING)
    private ParticipantRole participantRole;

    public static Participant create(Member member, MeetingPost meetingPost, ParticipantRole participantRole) {
        return builder()
                .participantRole(participantRole)
                .member(member)
                .meetingPost(meetingPost)
                .build();
    }

    public void addMeetingPost(MeetingPost meetingPost) {
        this.meetingPost = meetingPost;
    }
}
