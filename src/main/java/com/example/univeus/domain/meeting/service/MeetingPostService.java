package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.presentation.meeting.dto.request.MeetingUpdateRequest.MeetingPostUpdate;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostContent;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostUris;

public interface MeetingPostService {
    void writePost(Long memberId, MeetingPostContent meetingPostContent, MeetingPostUris meetingPostImages);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);

    void updatePost(Long memberId, Long postId, MeetingPostUpdate updateMeetingPost);
}
