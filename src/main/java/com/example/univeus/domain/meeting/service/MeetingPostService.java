package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.presentation.meeting.dto.request.MeetingUpdateRequest.MeetingPostUpdate;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostContent;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostUris;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostDto.MainPageResponse;

public interface MeetingPostService {
    void writePost(Long memberId, MeetingPostContent meetingPostContent, MeetingPostUris meetingPostImages);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);

    void updatePost(Long memberId, Long postId, MeetingPostUpdate updateMeetingPost);

    MainPageResponse getMeetingPosts(String cursor, String category, int size);

    MainPageResponse getMeetingPostsOffset(String category, int page, int size);
}
