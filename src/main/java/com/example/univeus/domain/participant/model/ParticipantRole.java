package com.example.univeus.domain.participant.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ParticipantRole {
    OWNER("owner"),
    PARTICIPANT("participant");

    private final String status;
}
