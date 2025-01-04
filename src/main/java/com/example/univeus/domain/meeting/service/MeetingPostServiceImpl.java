package com.example.univeus.domain.meeting.service;

import com.example.univeus.common.util.TimeUtil;
import com.example.univeus.domain.meeting.model.Coordinate;
import com.example.univeus.domain.meeting.model.Location;
import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.model.MeetingSchedule;
import com.example.univeus.domain.meeting.model.PostDeadline;
import com.example.univeus.domain.meeting.repository.MeetingPostImageRepository;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.meeting.dto.request.MeetingRequest;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingPostServiceImpl implements MeetingPostService {

    private final MeetingPostRepository meetingPostRepository;
    private final MeetingPostImageRepository meetingPostImageRepository;
    private final MemberService memberService;
    private final Clock clock;

    @Transactional
    public void writePost(Long writerId, MeetingRequest.WriteMeetingPost writeMeetingPost) {
        LocalDateTime now = LocalDateTime.now(clock);

        Member writer = memberService.findById(writerId);
        Coordinate coordinate = Coordinate.of(writeMeetingPost.latitude(), writeMeetingPost.longitude());
        Gender gender = Gender.of(writeMeetingPost.genderLimit());
        Location location = Location.of(writeMeetingPost.address(), coordinate);
        MeetingCategory meetingCategory = MeetingCategory.of(writeMeetingPost.meetingCategory());

        PostDeadline postDeadline = PostDeadline.of(
                TimeUtil.parseToLocalDateTime(writeMeetingPost.postDeadline()), now);
        MeetingSchedule meetingSchedule = MeetingSchedule.of(
                TimeUtil.parseToLocalDateTime(writeMeetingPost.meetingSchedule()), now, postDeadline);

        MeetingPost meetingPost = MeetingPost.create(
                writer,
                writeMeetingPost.title(),
                writeMeetingPost.body(),
                Integer.parseInt(writeMeetingPost.joinLimit()), gender
                , location, postDeadline, meetingSchedule, meetingCategory
        );

        meetingPostRepository.save(meetingPost);
    }
}
