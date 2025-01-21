package com.example.univeus.domain.chat.model;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ChatMessages")
@AllArgsConstructor
@Builder
@Getter
public class ChatMessage {

    @Id
    private ObjectId id;
    private Long chatRoomId;
    private String content;
    private String nickname;
    private String studentId;
    private Long senderId;

    public static ChatMessage create(Long chatRoomId, String content, Long senderId, String nickname, String studentId) {
        return ChatMessage.builder()
                .chatRoomId(chatRoomId)
                .content(content)
                .studentId(studentId)
                .nickname(nickname)
                .senderId(senderId)
                .build();
    }
}
