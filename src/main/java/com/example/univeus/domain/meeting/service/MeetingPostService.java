package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.presentation.meeting.dto.request.MeetingPostRequest;
import com.example.univeus.presentation.meeting.dto.response.MeetingPostResponse;

public interface MeetingPostService {
    void writePost(Long memberId, MeetingPostRequest.MeetingPostContent meetingPostContent, MeetingPostRequest.MeetingPostImagesUris meetingPostImages);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);

    void updatePost(Long memberId, Long postId, MeetingPostRequest.Update updateMeetingPost);

    MeetingPostResponse.MeetingPost readPost(Long memberId, Long aLong);
}
