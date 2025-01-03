package com.example.univeus.domain.member.model;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.member.exception.MemberException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Department {

    HUMANITY("인문대학"),
    SOCIAL_SCIENCE("사회과학대학"),
    ART_AND_PHYSICS("예술체육대학"),
    SOFTWARE_AND_BUSINESS("소프트웨어경영대학"),
    PURE_SCIENCE("융합과학대학"),
    ENGINEERING("창의공과대학"),
    TOURISM_AND_CULTURE("관광문화대학"),
    NONE("소속없음");


    private final String department;

    public static Department of(String department) {
        for (Department depart : Department.values()) {
            if (depart.department.equals(department)) {
                return depart;
            }
        }
        throw new MemberException(DEPARTMENT_NOT_FOUND);
    }
}
