package com.example.univeus.domain.participant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantStatus {
    PENDING("pending"),
    JOINING("joining");

    private final String status;
}
