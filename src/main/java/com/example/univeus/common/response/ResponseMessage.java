package com.example.univeus.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ResponseMessage {
    /**
     * auth
     */
    REFRESH_TOKEN_EXPIRED("AUTH-001", "만료된 refresh token 입니다."),
    REFRESH_TOKEN_NOT_FOUND("AUTH-002", "refresh token이 존재하지 않습니다."),
    ACCESS_TOKEN_NOT_FOUND("AUTH-002", "access token이 존재하지 않습니다."),
    AUTH_BAD_REQUEST("AUTH-003", "접근 권한이 없습니다."),
    REISSUE_TOKEN_SUCCESS("AUTH-004", "토큰 재발급을 성공했습니다."),
    LOGIN_SUCCESS("AUTH-005", "로그인을 성공했습니다."),
    LOGIN_FAIL("AUTH-006", "로그인에 실패했습니다"),
    PROFILE_REGISTER_SUCCESS("AUTH-007", "프로필 등록을 성공했습니다."),
    CHECK_NICKNAME_DUPLICATED_SUCCESS("AUTH-008", "닉네임 중복 검사를 성공했습니다"),
    INVALID_CERTIFICATION_NUMBER("AUTH-009", "인증번호가 올바르지 않습니다."),
    CERTIFICATION_BAD_REQUEST("AUTH-010", "인증 번호 요청을 다시 수행하세요"),
    CERTIFICATION_REQUEST_SUCCESS("AUTH-011", "인증번호 요청을 성공했습니다."),
    CERTIFICATION_VERIFY_SUCCESS("AUTH-012", "인증번호 검증을 성공했습니다."),

    /**
     * member
     */
    MEMBER_NOT_FOUND("MEMBER-001", "존재하지 않는 유저입니다."),
    MEMBER_NOT_AUTHORIZED_PHONE("MEMBER-002", "휴대폰 본인인증을 완료하지 않은 유저입니다."),
    MEMBER_NOT_AUTHORIZED_PROFILE("MEMBER-003", "프로필 등록을 완료하지 않은 유저입니다."),
    DEPARTMENT_NOT_FOUND("MEMBER-004", "존재하지 않는 학과입니다."),
    GENDER_NOT_FOUND("MEMBER-005", "존재하지 않는 성별입니다."),
    MEMBER_NICKNAME_DUPLICATED("MEMBER-006", "닉네임이 중복됩니다."),
    MEMBER_STUDENT_ID_FORMAT_EXCEPTION("MEMBER-007", "올바르지 않은 형식의 학번입니다."),
    MEMBER_NICKNAME_FORMAT_EXCEPTION("MEMBER-008", "올바르지 않은 형식의 닉네임입니다."),
    UPDATE_PHONE_NUMBER_SUCCESS("MEMBER-009", "휴대폰 번호 업데이트 성공했습니다.");


    private final String code;
    private final String message;
}
