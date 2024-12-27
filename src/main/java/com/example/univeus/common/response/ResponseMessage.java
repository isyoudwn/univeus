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

    /**
     * member
     */
    MEMBER_NOT_FOUND("MEMBER-001", "존재하지 않는 유저입니다.");

    private final String code;
    private final String message;
}
