package com.example.univeus.presentation.chat.controller;

import com.example.univeus.common.annotation.ChatAuth;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.chat.service.ChatService;
import com.example.univeus.domain.chat.service.ChattingMemberDetailDTO;
import com.example.univeus.presentation.chat.dto.ChatMessageRequest;
import com.example.univeus.presentation.chat.dto.ChatMessageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    // publish라는 prefix가 자동으로 붙는다.
    // publish/chat/roomId
    private final ChatService chatService;

    @MessageMapping("/chat/{roomId}")
    @SendTo("/subscribe/{roomId}")
    public ChatMessageResponse sendMessage(
            @ChatAuth Accessor accessor,
            @DestinationVariable String roomId,
            ChatMessageRequest messageRequest
    ) {
        Long memberId = accessor.getMemberId();
        ChattingMemberDetailDTO memberDetail = chatService.getMemberDetailById(memberId);
        chatService.saveMessage(roomId, messageRequest, memberDetail);

        return new ChatMessageResponse(
                memberDetail.nickName(),
                memberDetail.studentId(),
                messageRequest.content(),
                messageRequest.timestamp()
        );
    }
}
