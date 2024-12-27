package com.example.univeus.domain.member.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Department {

    HUMANITY("인문대학"),
    SOCIAL_SCIENCE("사회과학대학"),
    ART_AND_PHYSICS("예술체육대학"),
    SOFTWARE_AND_BUSINESS("소프트웨어경영대학"),
    PURE_SCIENCE("융합과학대학"),
    ENGINEERING("창의공과대학"),
    TOURISM_AND_CULTURE("관광문화대학");


    private final String department;
}
