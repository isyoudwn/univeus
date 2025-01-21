package com.example.univeus.domain.chat.service;

public record ChattingMemberDetailDTO(
        String userId,
        String nickName,
        String studentId
) {
    public static ChattingMemberDetailDTO of(String userId, String nickName, String studentId) {
        return new ChattingMemberDetailDTO(userId, nickName, studentId);
    }
}
