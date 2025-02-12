package com.example.univeus.presentation.meeting.dto.request;

import com.example.univeus.domain.meeting.model.MeetingCategory;

public class MainPageRequest {

    public record MainPageCursor(
            Long id,
            MeetingCategory meetingCategory
    ) {
    }

    public record MainPageOffset(
            String page,
            String category
    ) {
    }
}
