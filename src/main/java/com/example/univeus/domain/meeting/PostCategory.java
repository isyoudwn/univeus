package com.example.univeus.domain.meeting;


import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PostCategory {
    STUDY("스터디모집"),

    MEETING("모임");

    private final String category;
}
