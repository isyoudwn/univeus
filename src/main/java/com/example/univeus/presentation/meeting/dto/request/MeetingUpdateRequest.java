package com.example.univeus.presentation.meeting.dto.request;


import com.example.univeus.domain.meeting.service.dto.MeetingPostImageDTO;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostContent;
import com.example.univeus.presentation.meeting.dto.request.MeetingWriteRequest.MeetingPostUris;
import java.util.List;

public class MeetingUpdateRequest {
    public record MeetingPostUpdate(
            MeetingPostContent meetingPostContent,
            MeetingPostUris newMeetingPostUris,
            DeletedPostImages deletedPostImages
    ) {
    }

    public record DeletedPostImages(
            List<MeetingPostImageDTO> images
    ) {
    }
}
