package com.example.univeus.domain.member;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Membership {
    PRO("pro"),
    NORMAL("normal");

    public final String membership;
}
