package com.example.univeus.domain.chat.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ChatMessages")
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    private ObjectId id;
    private Long chatRoomId;
    private String content;
    private Long senderId;

    public static ChatMessage create(Long chatRoomId, String content, Long senderId) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .content(content)
                .senderId(senderId)
                .build();
    }
}
