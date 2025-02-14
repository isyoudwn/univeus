package com.example.univeus.domain.member.model;

import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.member.exception.MemberException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Gender {
    WOMAN("WOMAN"),
    MAN("MAN"),
    NONE("NONE");

    private final String gender;

    public static Gender of(String value) {
        for (Gender gender : Gender.values()) {
            if (gender.gender.equals(value)) {
                return gender;
            }
        }
        throw new MemberException(GENDER_NOT_FOUND);
    }
}
