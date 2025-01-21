package com.example.univeus.domain.meeting.model;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.participant.Participant;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
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

    @OneToMany(mappedBy = "meetingPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MeetingPostImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "meetingPost", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<Participant> participants = new ArrayList<>();


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

    public void update(String title, String body,
                       Integer joinLimit, Gender genderLimit,
                       Location location, PostDeadline postDeadLine,
                       MeetingSchedule meetingSchedule, MeetingCategory meetingCategory,
                       List<MeetingPostImage> meetingPostImages) {
        this.title = title;
        this.body = body;
        this.joinLimit = joinLimit;
        this.genderLimit = genderLimit;
        this.location = location;
        this.postDeadLine = postDeadLine;
        this.meetingSchedule = meetingSchedule;
        this.meetingCategory = meetingCategory;
        this.images = meetingPostImages;
    }

    public void addImage(MeetingPostImage meetingPostImage) {
        this.images.add(meetingPostImage);
        meetingPostImage.addMeetingPost(this);
    }

    public void addParticipant(Participant participant) {
        this.participants.add(participant);
        participant.addMeetingPost(this);
    }

    public void deleteImages(Long imageId) {
        for (MeetingPostImage image : images) {
            if (Objects.equals(image.getId(), imageId)) {
                this.images.remove(image);
                return;
            }
        }
    }
}
