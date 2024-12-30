package com.example.univeus.domain.meeting.model;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostCategory {
    STUDY("스터디모집"),

    MEETING("모임");

    private final String category;
}
