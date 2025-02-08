package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.presentation.meeting.dto.request.MeetingPostRequest;
import com.example.univeus.presentation.meeting.dto.response.MainPageResponse;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostResponse;

public interface MeetingPostService {
    void writePost(Long memberId, MeetingPostRequest.MeetingPostContent meetingPostContent, MeetingPostRequest.MeetingPostImagesUris meetingPostImages);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);

    void updatePost(Long memberId, Long postId, MeetingPostRequest.Update updateMeetingPost);

    MainPageResponse.MainPage getMeetingPosts(String cursor, String category, int size);

    MainPageResponse.MainPage getMeetingPostsOffset(String category, int page, int size);

    MeetingPostResponse.MeetingPost readPost(Long memberId, Long aLong);
}
