package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.presentation.meeting.dto.request.MeetingRequest.MeetingPostWriteAndUpdate;

public interface MeetingPostService {
    void writePost(Long memberId, MeetingPostWriteAndUpdate writeMeetingPost);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);

    void updatePost(Long memberId, Long postId, MeetingPostWriteAndUpdate updateMeetingPost);
}
