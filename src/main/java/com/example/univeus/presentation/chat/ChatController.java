package com.example.univeus.presentation.chat;

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
    @MessageMapping("/chat/{roomId}")
    @SendTo("/subscribe/{roomId}")
    public ChatMessageResponse sendMessage(
            @DestinationVariable String roomId,
            ChatMessageRequest messageRequest
    ) {
        return new ChatMessageResponse(
                messageRequest.userName(),
                messageRequest.content(),
                messageRequest.timestamp()
        );
    }
}
