package com.example.univeus.presentation.chat.dto;

public record ChatMessageRequest(
        String content,
        String timestamp
) {
}
