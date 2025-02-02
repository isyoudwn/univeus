package com.example.univeus.presentation.meeting.dto.request;

public class MainPageRequest {

    public record MainPageCursor(
            String id,
            String category,
            String size
    ) {
    }

    public record MainPageOffset(
            String page,
            String size,
            String category
    ) {
    }
}
