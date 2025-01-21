package com.example.univeus.domain.participant;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantRole {
    OWNER("owner"),
    PARTICIPANT("participant");

    private final String status;
}
