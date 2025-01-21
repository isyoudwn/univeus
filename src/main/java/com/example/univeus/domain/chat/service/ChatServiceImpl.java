package com.example.univeus.domain.chat.service;

import com.example.univeus.domain.chat.model.ChatMessage;
import com.example.univeus.domain.chat.repository.ChatMessageRepository;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.chat.dto.ChatMessageRequest;
import com.example.univeus.presentation.chat.dto.ChatMessageResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final MemberService memberService;

    @Override
    @Async
    public void saveMessage(String roomId, ChatMessageRequest chatMessageRequest,
                            ChattingMemberDetailDTO memberDetail) {
        ChatMessage chatMessage =
                ChatMessage.create(
                        Long.valueOf(roomId),
                        chatMessageRequest.content(),
                        Long.valueOf(memberDetail.userId()),
                        memberDetail.nickName(),
                        memberDetail.studentId()
                );
        chatMessageRepository.save(chatMessage);
    }

    /**
     * 캐시가 존재한다면 : 먼저 "chattingMemberDetailCache" 캐시에서 키 "'chat:member:' + #userId"에 해당하는 값을 찾습니다. 캐시에 값이 존재하면,
     * getMemberDetailById 메서드는 실행되지 않고 캐시된 값이 즉시 반환됩니다. 캐시가 없을 때 : getMemberDetailById 메서드가 호출
     * memberService.findById(userId)를 통해 Member 객체를 조회. 생성된 ChattingMemberDetailDTO 객체가 반환됩니다. 반환된 객체는 자동으로 캐시에 저장됩니다.
     * 캐시 키는 'chat:member:' + userId 형식이 됩니다.
     **/

    @Override
    @Cacheable(cacheNames = "chattingMemberDetailCache", key = "'chat:member:' + #userId", cacheManager = "chattingMemberDetailCacheManager")
    public ChattingMemberDetailDTO getMemberDetailById(Long userId) {
        Member member = memberService.findById(userId);
        return ChattingMemberDetailDTO.of(userId.toString(), member.getNickname(), member.getStudentId());
    }

    @Override
    public List<ChatMessageResponse> getMessages(Long postId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByPostId(postId);

        return chatMessages.stream()
                .map(chatMessage -> new ChatMessageResponse(chatMessage.getStudentId(), chatMessage.getNickname(),
                        chatMessage.getContent(), "10:11"))
                .collect(Collectors.toList());
    }
}
