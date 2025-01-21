package com.example.univeus.presentation.chat.controller;

import com.example.univeus.common.annotation.ChatAuth;
import com.example.univeus.common.response.Response;
import com.example.univeus.common.response.ResponseMessage;
import com.example.univeus.domain.auth.model.Accessor;
import com.example.univeus.domain.chat.service.ChatService;
import com.example.univeus.domain.chat.service.ChattingMemberDetailDTO;
import com.example.univeus.presentation.chat.dto.ChatMessageRequest;
import com.example.univeus.presentation.chat.dto.ChatMessageResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/chat/{roomId}")
    public ResponseEntity<Response<List<ChatMessageResponse>>> readMessages(
            @PathVariable String roomId) {
        List<ChatMessageResponse> chatMessages = chatService.getMessages(Long.valueOf(roomId));
        return ResponseEntity
                .ok()
                .body(Response.success(
                        ResponseMessage.READING_CHATTING_HISTORY_SUCCESS.getCode(),
                        ResponseMessage.READING_CHATTING_HISTORY_SUCCESS.getMessage(),
                        chatMessages
                ));
    }
}
