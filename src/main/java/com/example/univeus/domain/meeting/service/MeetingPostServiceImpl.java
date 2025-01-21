package com.example.univeus.domain.meeting.service;

import static com.example.univeus.common.response.ResponseMessage.MEETING_POST_NOT_FOUND;

import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.model.MeetingPostImage;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import com.example.univeus.domain.meeting.service.dto.MeetingPostDTO.MeetingPostDetailDTO;
import com.example.univeus.domain.meeting.service.dto.mapper.MeetingPostMapper;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.domain.meeting.service.dto.MeetingPostImageDTO;
import com.example.univeus.domain.participant.Participant;
import com.example.univeus.domain.participant.ParticipantRole;
import com.example.univeus.presentation.meeting.dto.request.MeetingUpdateRequest.DeletedPostImages;
import com.example.univeus.presentation.meeting.dto.request.MeetingUpdateRequest.MeetingPostUpdate;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostContent;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostUris;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MeetingPostServiceImpl implements MeetingPostService {

    private final MeetingPostRepository meetingPostRepository;
    private final MemberService memberService;
    private final Clock clock;

    @Override
    @Transactional
    public void deletePost(Long memberId, Long postId) {
        Member currentMember = memberService.findById(memberId);
        MeetingPost meetingPost = findById(postId);

        currentMember.isMine(meetingPost.getWriter());

        meetingPostRepository.delete(meetingPost);
    }

    @Override
    @Transactional
    public void writePost(Long writerId, MeetingPostContent meetingPostContent, MeetingPostUris meetingPostUris) {
        LocalDateTime now = LocalDateTime.now(clock);
        Member currentMember = memberService.findById(writerId);
        MeetingPost meetingPost = MeetingPostMapper.toMeetingPost(currentMember, meetingPostContent, now);
        Participant participant  = Participant.create(currentMember, meetingPost, ParticipantRole.OWNER);

        addImages(meetingPost, meetingPostUris);
        meetingPost.addParticipant(participant);

        meetingPostRepository.save(meetingPost);
    }

    @Override
    @Transactional(readOnly = true)
    public MeetingPost findById(Long postId) {
        return meetingPostRepository.findById(postId)
                .orElseThrow(() -> new MeetingException(MEETING_POST_NOT_FOUND));
    }

    @Override
    @Transactional
    public void updatePost(Long memberId, Long postId, MeetingPostUpdate meetingPostUpdate) {
        Member currentMember = memberService.findById(memberId);
        MeetingPost meetingPost = findById(postId);
        currentMember.isMine(meetingPost.getWriter());

        deleteImages(meetingPost, meetingPostUpdate.deletedPostImages());
        addImages(meetingPost, meetingPostUpdate.newMeetingPostUris());

        MeetingPostContent meetingPostContent = meetingPostUpdate.meetingPostContent();
        LocalDateTime now = LocalDateTime.now(clock);
        MeetingPostDetailDTO meetingPostDetail = MeetingPostMapper.toMeetingPostDetail(
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
                meetingPost.getImages()
        );
    }

    private void deleteImages(MeetingPost meetingPost, DeletedPostImages deletedPostImages) {
        // 상위 메서드의 트랜잭션에 합류
        for (MeetingPostImageDTO image : deletedPostImages.images()) {
            Long postImageId = Long.valueOf(image.id());
            meetingPost.deleteImages(postImageId);
        }
    }

    private void addImages(MeetingPost meetingPost, MeetingPostUris meetingPostUris) {
        // 상위 메서드의 트랜잭션에 합류
        for (String uri : meetingPostUris.uris()) {
            MeetingPostImage meetingPostImage = MeetingPostImage.create(uri);
            meetingPost.addImage(meetingPostImage);
        }
    }
}
