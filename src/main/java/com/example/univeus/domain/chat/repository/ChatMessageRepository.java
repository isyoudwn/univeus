package com.example.univeus.domain.chat.repository;

import com.example.univeus.domain.chat.model.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ChatMessageRepository extends MongoRepository<ChatMessage, String> {
}
