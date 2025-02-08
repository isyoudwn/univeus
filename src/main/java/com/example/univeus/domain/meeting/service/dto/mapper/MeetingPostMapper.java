package com.example.univeus.domain.meeting.service.dto.mapper;

import com.example.univeus.common.util.TimeUtil;
import com.example.univeus.domain.meeting.model.Coordinate;
import com.example.univeus.domain.meeting.model.Location;
import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.model.MeetingSchedule;
import com.example.univeus.domain.meeting.model.PostDeadline;
import com.example.univeus.domain.meeting.service.dto.MeetingPostDTO.MeetingPostDetailDTO;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.presentation.meeting.dto.request.MeetingRequest;
import java.time.LocalDateTime;

public class MeetingPostMapper {

    public static Coordinate toCoordinate(String latitude, String longitude) {
        return Coordinate.of(latitude, longitude);
    }

    public static Gender toGender(String genderLimit) {
        return Gender.of(genderLimit);
    }

    public static Location toLocation(String address, Coordinate coordinate) {
        return Location.of(address, coordinate);
    }

    public static MeetingCategory toMeetingCategory(String meetingCategory) {
        return MeetingCategory.of(meetingCategory);
    }

    public static PostDeadline toPostDeadline(String postDeadline, LocalDateTime now) {
        return PostDeadline.of(TimeUtil.parseToLocalDateTime(postDeadline), now);
    }

    public static MeetingSchedule toMeetingSchedule(String meetingSchedule, PostDeadline postDeadline,
                                                    LocalDateTime now) {
        return MeetingSchedule.of(TimeUtil.parseToLocalDateTime(meetingSchedule), now, postDeadline);
    }

    public static MeetingPost toMeetingPost(Member member, MeetingRequest.MeetingPostContent meetingPostContent, LocalDateTime now) {
        Coordinate coordinate = toCoordinate(meetingPostContent.latitude(), meetingPostContent.longitude());
        Gender gender = toGender(meetingPostContent.genderLimit());
        Location location = toLocation(meetingPostContent.address(), coordinate);
        MeetingCategory meetingCategory = toMeetingCategory(meetingPostContent.meetingCategory());
        PostDeadline postDeadline = toPostDeadline(meetingPostContent.postDeadline(), now);
        MeetingSchedule meetingSchedule = toMeetingSchedule(meetingPostContent.meetingSchedule(), postDeadline, now);

        return MeetingPost.create(
                member,
                meetingPostContent.title(),
                meetingPostContent.body(),
                Integer.parseInt(meetingPostContent.joinLimit()),
                gender,
                location,
                postDeadline,
                meetingSchedule,
                meetingCategory);
    }

    public static MeetingPostDetailDTO toMeetingPostDetail(MeetingRequest.MeetingPostContent meetingPostContent, LocalDateTime now) {

        Coordinate coordinate = toCoordinate(meetingPostContent.latitude(), meetingPostContent.longitude());
        Gender gender = toGender(meetingPostContent.genderLimit());
        Location location = toLocation(meetingPostContent.address(), coordinate);
        MeetingCategory meetingCategory = toMeetingCategory(meetingPostContent.meetingCategory());
        PostDeadline postDeadline = toPostDeadline(meetingPostContent.postDeadline(), now);
        MeetingSchedule meetingSchedule = toMeetingSchedule(meetingPostContent.meetingSchedule(), postDeadline, now);

        return new MeetingPostDetailDTO(
                meetingPostContent.title(),
                meetingPostContent.body(),
                gender,
                Integer.parseInt(meetingPostContent.joinLimit()),
                meetingCategory,
                postDeadline,
                meetingSchedule,
                location);
    }
}
