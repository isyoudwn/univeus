package com.example.univeus.domain.meeting.service.dto.mapper;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.model.MeetingPostImage;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse.MainPageDetail;
import com.example.univeus.presentation.member.dto.request.MemberDto.Profile;
import java.util.List;

public class MainPageMapper {

    public static MainPageDetail toMainPageDetail(MeetingPost meetingPost) {
        Member writer = meetingPost.getWriter();
        Profile profile = Profile.of(
                writer.getNickname(), writer.getDepartment().getDepartment(),
                writer.getGender().getGender(), writer.getStudentId());

        List<MeetingPostImage> images = meetingPost.getImages();
        String thumbNail = "none";

        if (images.size() != 0) {
            thumbNail = images.get(0).getUri();
        }

        return new MainPageDetail(
                meetingPost.getTitle(),
                meetingPost.getJoinLimit(),
                meetingPost.getMeetingPostStatus(),
                meetingPost.getGenderLimit().getGender(),
                meetingPost.getPostDeadLine().getPostDeadline(),
                meetingPost.getMeetingSchedule().getMeetingSchedule(),
                meetingPost.getMeetingCategory(),
                profile,
                thumbNail);
    }
}
