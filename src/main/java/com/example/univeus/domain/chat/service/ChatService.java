package com.example.univeus.domain.chat.service;

import com.example.univeus.presentation.chat.dto.ChatMessageRequest;

public interface ChatService {

    void saveMessage(String roomId, ChatMessageRequest chatMessageRequest, ChattingMemberDetailDTO memberDetail);

    ChattingMemberDetailDTO getMemberDetailById(Long memberId);
}
