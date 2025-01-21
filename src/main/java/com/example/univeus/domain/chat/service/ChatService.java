package com.example.univeus.domain.chat.service;

import com.example.univeus.presentation.chat.dto.ChatMessageRequest;
import com.example.univeus.presentation.chat.dto.ChatMessageResponse;
import java.util.List;

public interface ChatService {

    void saveMessage(String roomId, ChatMessageRequest chatMessageRequest, ChattingMemberDetailDTO memberDetail);

    ChattingMemberDetailDTO getMemberDetailById(Long memberId);

    List<ChatMessageResponse> getMessages(Long chatRoomId);
}
