package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse.MainPageDetail;
import com.example.univeus.presentation.member.dto.request.MemberDto.Profile;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MainPageServiceImpl implements MainPageService {
    private static final Integer SIZE = 20;
    private final MeetingPostRepository meetingPostRepository;

    @Override
    public MainPageResponse.MainPage getMainPage(Long cursor, MeetingCategory meetingCategory) {
        Pageable pageable = PageRequest.of(0, SIZE); // 페이지 크기만 지정
        List<MeetingPost> meetingPosts = meetingPostRepository.findByCategoryAndOptionalCursor(meetingCategory,
                cursor,
                pageable);

        List<MainPageDetail> mainPages = meetingPosts.stream().map(meetingPost -> {
            Member writer = meetingPost.getWriter();
            Profile profile = Profile.of(
                    writer.getNickname(), writer.getDepartment().getDepartment(),
                    writer.getGender().getGender(), writer.getStudentId());

            return new MainPageDetail(
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
            return new MainPageResponse.MainPage(mainPages, "NONE", false);
        }

        String nextCursor = meetingPosts.getLast().getId().toString();
        return new MainPageResponse.MainPage(mainPages, nextCursor, true);
    }

    @Override
    public MainPageResponse.MainPage getMainPageOffset(String category, int page) {
        Pageable pageable = PageRequest.of(page, SIZE);
        MeetingCategory meetingCategory = (!category.equals("none")) ? MeetingCategory.of(category) : null;

        Page<MeetingPost> meetingPostPage = meetingPostRepository.findByCategoryAndOffset(meetingCategory, pageable);

        List<MainPageResponse.MainPageDetail> result = meetingPostPage.getContent().stream().map(meetingPost -> {

            Member writer = meetingPost.getWriter();
            Profile profile = Profile.of(
                    writer.getNickname(),
                    writer.getDepartment().getDepartment(),
                    writer.getGender().getGender(),
                    writer.getStudentId()
            );

            return new MainPageResponse.MainPageDetail(
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

        return new MainPageResponse.MainPage(result, String.valueOf(nextPage), hasNext);
    }
}
