package com.example.univeus.domain.meeting.service;

import com.example.univeus.presentation.meeting.dto.request.MeetingRequest.WriteMeetingPost;

public interface MeetingPostService {
    void writePost(Long memberId, WriteMeetingPost writeMeetingPost);
}
