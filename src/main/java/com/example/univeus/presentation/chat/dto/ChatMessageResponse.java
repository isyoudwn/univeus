package com.example.univeus.presentation.chat.dto;

public record ChatMessageResponse(
        String userName,
        String content,
        String timestamp
) {
}
