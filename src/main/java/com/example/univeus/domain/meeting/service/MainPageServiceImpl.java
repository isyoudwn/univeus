package com.example.univeus.domain.meeting.service;

import static com.example.univeus.presentation.meeting.dto.response.MainPageResponse.*;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.repository.MeetingPostRepository;
import com.example.univeus.domain.meeting.service.dto.mapper.MainPageMapper;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse.MainPageDetail;
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
    public MainPage getMainPage(Long cursor, MeetingCategory meetingCategory) {
        List<MeetingPost> meetingPosts = meetingPostRepository.findByCategoryAndOptionalCursor(meetingCategory,
                cursor);

        List<MainPageDetail> mainPages = meetingPosts.stream().map(MainPageMapper::toMainPageDetail).toList();

        if (meetingPosts.isEmpty()) {
            return new MainPage(mainPages, "NONE", false);
        }

        String nextCursor = String.valueOf(meetingPosts.get(meetingPosts.size() - 1).getId());
        return new MainPage(mainPages, nextCursor, true);
    }

    @Override
    public MainPage getMainPageOffset(String category, int page) {
        Pageable pageable = PageRequest.of(page, SIZE);
        MeetingCategory meetingCategory = (!category.equals("none")) ? MeetingCategory.of(category) : null;

        Page<MeetingPost> meetingPostPage = meetingPostRepository.findByCategoryAndOffset(meetingCategory, pageable);
        List<MainPageDetail> mainPages = meetingPostPage.stream().map(MainPageMapper::toMainPageDetail).toList();

        boolean hasNext = meetingPostPage.hasNext();
        int nextPage = hasNext ? page + 1 : -1;

        return new MainPage(mainPages, String.valueOf(nextPage), hasNext);
    }
}
