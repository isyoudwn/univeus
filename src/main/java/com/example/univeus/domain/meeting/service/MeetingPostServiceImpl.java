package com.example.univeus.domain.meeting.service;

import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import com.example.univeus.domain.meeting.service.dto.MeetingPostDTO.MeetingPostDetailDTO;
import com.example.univeus.domain.meeting.service.dto.mapper.MeetingPostMapper;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.meeting.dto.request.MeetingRequest.MeetingPostWriteAndUpdate;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MeetingPostServiceImpl implements MeetingPostService {

    private final MeetingPostRepository meetingPostRepository;
    private final MemberService memberService;
    private final Clock clock;

    @Override
    @Transactional
    public void writePost(Long writerId, MeetingPostWriteAndUpdate writeMeetingPost) {
        LocalDateTime now = LocalDateTime.now(clock);
        Member writer = memberService.findById(writerId);
        MeetingPost meetingPost = MeetingPostMapper.toMeetingPost(writer, writeMeetingPost, now);

        meetingPostRepository.save(meetingPost);
    }

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Member writer = memberService.findById(memberId);
        MeetingPost meetingPost = findById(postId);

        if (!Objects.equals(meetingPost.getWriter().getId(), writer.getId())) {
            throw new MeetingException(ResponseMessage.MEETING_BAD_REQUEST);
        }
        meetingPostRepository.delete(meetingPost);
    }

    @Override
    public MeetingPost findById(Long postId) {
        return meetingPostRepository.findById(postId)
                .orElseThrow(() -> new MeetingException(ResponseMessage.MEETING_POST_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updatePost(Long memberId, Long postId, MeetingPostWriteAndUpdate updateMeetingPost) {
        Member writer = memberService.findById(memberId);
        MeetingPost meetingPost = findById(postId);

        if (!Objects.equals(meetingPost.getWriter().getId(), writer.getId())) {
            throw new MeetingException(ResponseMessage.MEETING_BAD_REQUEST);
        }
        LocalDateTime now = LocalDateTime.now(clock);
        MeetingPostDetailDTO meetingPostDetail = MeetingPostMapper.toMeetingPostDetail(updateMeetingPost, now);

        meetingPost.update(
                meetingPostDetail.title(),
                meetingPostDetail.body(),
                meetingPostDetail.joinLimit(),
                meetingPostDetail.genderLimit(),
                meetingPostDetail.location(),
                meetingPostDetail.postDeadline(),
                meetingPostDetail.meetingSchedule(),
                meetingPostDetail.meetingCategory()
        );
    }
}
