package com.example.univeus.presentation.meeting.dto.response;

import com.example.univeus.domain.meeting.service.dto.MeetingPostDTO.MeetingPostDetail;
import java.util.List;

public class MeetingPostResponse {
    public record MeetingPost(
            MeetingPostDetail meetingPostDetail,
            Boolean isMyPost,
            List<String> uris
    ) {
    }
}
