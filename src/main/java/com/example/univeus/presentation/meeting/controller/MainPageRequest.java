package com.example.univeus.presentation.meeting.controller;

public class MainPageRequest {

    public record MainPagePage(
            String id,
            String size
    ) {
    }

    public record MainPagePageOffset(
            String page,
            String size
    ) {
    }
}
