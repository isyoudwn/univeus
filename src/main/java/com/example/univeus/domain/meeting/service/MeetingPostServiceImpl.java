package com.example.univeus.domain.meeting.service;

import static com.example.univeus.common.response.ResponseMessage.MEETING_POST_NOT_FOUND;

import com.example.univeus.common.util.TimeUtil;
import com.example.univeus.domain.meeting.exception.MeetingException;
import com.example.univeus.domain.meeting.model.MeetingCategory;
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
import com.example.univeus.domain.scheduler.service.QuartzService;
import com.example.univeus.presentation.meeting.dto.request.MeetingUpdateRequest.DeletedPostImages;
import com.example.univeus.presentation.meeting.dto.request.MeetingUpdateRequest.MeetingPostUpdate;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostContent;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostUris;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostDto;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostDto.MainPage;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostDto.MainPageResponse;
import com.example.univeus.presentation.member.dto.request.MemberDto.Profile;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

        currentMember.isMine(meetingPost.getWriter());

        meetingPostRepository.delete(meetingPost);
        quartzService.deleteJob(postId.toString());
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

        MeetingPost saved = meetingPostRepository.save(meetingPost);

        quartzService.scheduleDeadlineEvent(saved.getId().toString(), TimeUtil.localDateTimeToDate(saved.getPostDeadLine().getPostDeadline(), clock));
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
                meetingPost.getImages(),
                meetingPost.getMeetingPostStatus()
        );

        quartzService.updateSchedule(postId.toString(), TimeUtil.localDateTimeToDate(meetingPostDetail.postDeadline().getPostDeadline(), clock));
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

    @Override
    public MainPageResponse getMeetingPosts(String cursor, String category, int size) {
        Pageable pageable = PageRequest.of(0, size); // 페이지 크기만 지정

        MeetingCategory meetingCategory = (!category.equals("none")) ? MeetingCategory.of(category) : null;
        Long findCursor = (!cursor.equals("none")) ? Long.valueOf(cursor) : null;

        List<MeetingPost> meetingPosts = meetingPostRepository.findByCategoryAndOptionalCursor(meetingCategory, findCursor,
                pageable);

        List<MainPage> mainPages = meetingPosts.stream().map(meetingPost -> {
            Member writer = meetingPost.getWriter();
            Profile profile = Profile.of(
                    writer.getNickname(), writer.getDepartment().getDepartment(),
                    writer.getGender().getGender(), writer.getStudentId());

            return new MainPage(
                    meetingPost.getTitle(),
                    meetingPost.getJoinLimit(),
                    meetingPost.getMeetingPostStatus(),
                    meetingPost.getGenderLimit().getGender(),
                    meetingPost.getPostDeadLine().getPostDeadline(),
                    meetingPost.getMeetingSchedule().getMeetingSchedule(),
                    meetingPost.getMeetingCategory(),
                    profile);
        }).toList();

        if (meetingPosts.isEmpty()) {
            return new MainPageResponse(mainPages, "NONE", false);
        }

        String nextCursor = meetingPosts.getLast().getId().toString();
        return new MainPageResponse(mainPages, nextCursor, true);
    }

    @Override
    public MainPageResponse getMeetingPostsOffset(String category, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        MeetingCategory meetingCategory = (!category.equals("none")) ? MeetingCategory.of(category) : null;

        Page<MeetingPost> meetingPostPage = meetingPostRepository.findByCategoryAndOffset(meetingCategory, pageable);

        List<MeetingPostDto.MainPage> result = meetingPostPage.getContent().stream().map(meetingPost -> {

            Member writer = meetingPost.getWriter();
            Profile profile = Profile.of(
                    writer.getNickname(),
                    writer.getDepartment().getDepartment(),
                    writer.getGender().getGender(),
                    writer.getStudentId()
            );

            return new MeetingPostDto.MainPage(
                    meetingPost.getTitle(),
                    meetingPost.getJoinLimit(),
                    meetingPost.getMeetingPostStatus(),
                    meetingPost.getGenderLimit().getGender(),
                    meetingPost.getPostDeadLine().getPostDeadline(),
                    meetingPost.getMeetingSchedule().getMeetingSchedule(),
                    meetingPost.getMeetingCategory(),
                    profile
            );
        }).toList();

        boolean hasNext = meetingPostPage.hasNext();
        int nextPage = hasNext ? page + 1 : -1;

        return new MainPageResponse(result, String.valueOf(nextPage), hasNext);
    }
}
