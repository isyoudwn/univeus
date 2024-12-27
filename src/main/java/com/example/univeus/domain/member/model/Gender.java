package com.example.univeus.domain.member.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
    WOMAN("여자"),
    MAN("남자"),
    NONE("성별 상관 없음");

    private final String gender;
}
