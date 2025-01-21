package com.example.univeus.domain.meeting.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum MeetingPostStatus {
    RECRUITING("RECRUITING"),
    CLOSED("CLOSED");

    private final String status;
}
