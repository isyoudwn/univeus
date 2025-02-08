package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.service.dto.MeetingPostDTO.MeetingPostDetailResponse;
import com.example.univeus.presentation.meeting.dto.request.MeetingRequest;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostDto.MainPageResponse;

public interface MeetingPostService {
    void writePost(Long memberId, MeetingRequest.MeetingPostContent meetingPostContent, MeetingRequest.MeetingPostImagesUris meetingPostImages);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);

    void updatePost(Long memberId, Long postId, MeetingRequest.Update updateMeetingPost);

    MainPageResponse getMeetingPosts(String cursor, String category, int size);

    MainPageResponse getMeetingPostsOffset(String category, int page, int size);

    MeetingPostDetailResponse readPost(Long memberId, Long aLong);
}
