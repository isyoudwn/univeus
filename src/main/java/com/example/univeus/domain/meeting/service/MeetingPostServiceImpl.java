package com.example.univeus.domain.meeting.service;

import static com.example.univeus.common.response.ResponseMessage.MEETING_POST_NOT_FOUND;
import static com.example.univeus.common.response.ResponseMessage.MEMBER_BAD_REQUEST;

import com.example.univeus.common.util.TimeUtil;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.model.MeetingPostImage;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import com.example.univeus.domain.meeting.service.dto.MeetingPostDTO.MeetingPostDetail;
import com.example.univeus.domain.meeting.service.dto.mapper.MeetingPostMapper;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.domain.participant.model.Participant;
import com.example.univeus.domain.participant.model.ParticipantRole;
import com.example.univeus.domain.scheduler.service.QuartzService;
import com.example.univeus.presentation.meeting.dto.request.MeetingPostRequest;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostResponse;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingPostServiceImpl implements MeetingPostService {

    private final MeetingPostRepository meetingPostRepository;
    private final MemberService memberService;
    private final QuartzService quartzService;
    private final Clock clock;

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Member currentMember = memberService.findById(memberId);
        MeetingPost meetingPost = findById(postId);

        if (!currentMember.isMine(meetingPost.getWriter())) {
            throw new MemberException(MEMBER_BAD_REQUEST);
        }

        meetingPostRepository.delete(meetingPost);
        quartzService.deleteJob(postId.toString());
    }

    @Override
    @Transactional
    public void writePost(Long writerId, MeetingPostRequest.MeetingPostContent meetingPostContent, MeetingPostRequest.MeetingPostImagesUris meetingPostUris) {
        LocalDateTime now = LocalDateTime.now(clock);
        Member currentMember = memberService.findById(writerId);
        MeetingPost meetingPost = MeetingPostMapper.toMeetingPost(currentMember, meetingPostContent, now);
        Participant participant  = Participant.create(currentMember, meetingPost, ParticipantRole.OWNER);

        addImages(meetingPost, meetingPostUris);
        meetingPost.addParticipant(participant);

        MeetingPost saved = meetingPostRepository.save(meetingPost);

        quartzService.scheduleDeadlineEvent(saved.getId().toString(), TimeUtil.localDateTimeToDate(saved.getPostDeadLine().getPostDeadline(), clock));
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingPost findById(Long postId) {
        return meetingPostRepository.findByIdWithInfo(postId)
                .orElseThrow(() -> new MeetingException(MEETING_POST_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updatePost(Long memberId, Long postId, MeetingPostRequest.Update meetingPostUpdate) {
        Member currentMember = memberService.findById(memberId);
        MeetingPost meetingPost = findById(postId);

        if (!currentMember.isMine(meetingPost.getWriter())) {
            throw new MemberException(MEMBER_BAD_REQUEST);
        }

        deleteImages(meetingPost, meetingPostUpdate.deletedPostImages());
        addImages(meetingPost, meetingPostUpdate.newMeetingPostUris());

        MeetingPostRequest.MeetingPostContent meetingPostContent = meetingPostUpdate.meetingPostContent();
        LocalDateTime now = LocalDateTime.now(clock);
        MeetingPostDetail meetingPostDetail = MeetingPostMapper.toMeetingPostDetail(
                meetingPostContent, now);

        meetingPost.update(
                meetingPostDetail.title(),
                meetingPostDetail.body(),
                meetingPostDetail.joinLimit(),
                meetingPostDetail.genderLimit(),
                meetingPostDetail.location(),
                meetingPostDetail.postDeadline(),
                meetingPostDetail.meetingSchedule(),
                meetingPostDetail.meetingCategory(),
                meetingPost.getImages(),
                meetingPost.getMeetingPostStatus()
        );

        quartzService.updateSchedule(postId.toString(), TimeUtil.localDateTimeToDate(meetingPostDetail.postDeadline().getPostDeadline(), clock));
    }

    private void deleteImages(MeetingPost meetingPost, MeetingPostRequest.DeletedPostImages deletedPostImages) {
        // 상위 메서드의 트랜잭션에 합류
        for (String id : deletedPostImages.ids()) {
            Long postImageId = Long.valueOf(id);
            meetingPost.deleteImages(postImageId);
        }
    }

    private void addImages(MeetingPost meetingPost, MeetingPostRequest.MeetingPostImagesUris meetingPostUris) {
        // 상위 메서드의 트랜잭션에 합류
        for (String uri : meetingPostUris.uris()) {
            MeetingPostImage meetingPostImage = MeetingPostImage.create(uri);
            meetingPost.addImage(meetingPostImage);
        }
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingPostResponse.MeetingPost readPost(Long memberId, Long postId) {
        MeetingPost meetingPost = findById(postId);
        Member currentMember = memberService.findById(memberId);
        Boolean isMine = currentMember.isMine(meetingPost.getWriter());

        MeetingPostDetail meetingPostDetail = new MeetingPostDetail(
                meetingPost.getTitle(),
                meetingPost.getBody(),
                meetingPost.getGenderLimit(),
                meetingPost.getJoinLimit(),
                meetingPost.getMeetingCategory(),
                meetingPost.getPostDeadLine(),
                meetingPost.getMeetingSchedule(),
                meetingPost.getLocation()
        );

        List<String> imageUris = meetingPost.getImages()
                .stream()
                .map(MeetingPostImage::getUri)
                .toList();

        return new MeetingPostResponse.MeetingPost(meetingPostDetail, isMine, imageUris);
    }
}
