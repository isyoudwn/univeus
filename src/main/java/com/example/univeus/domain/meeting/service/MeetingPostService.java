package com.example.univeus.domain.meeting.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.presentation.meeting.dto.request.MeetingRequest.WriteMeetingPost;

public interface MeetingPostService {
    void writePost(Long memberId, WriteMeetingPost writeMeetingPost);

    void deletePost(Long memberId, Long postId);

    MeetingPost findById(Long postId);
}
