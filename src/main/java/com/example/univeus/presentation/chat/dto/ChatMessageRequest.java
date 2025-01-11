package com.example.univeus.presentation.chat.dto;

public record ChatMessageRequest(
        Long userId,
        String userName,
        String content,
        String timestamp
) {
}
