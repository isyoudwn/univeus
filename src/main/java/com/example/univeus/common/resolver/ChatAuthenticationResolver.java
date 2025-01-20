package com.example.univeus.common.resolver;

import com.example.univeus.common.annotation.ChatAuth;
import com.example.univeus.domain.auth.TokenExtractor;
import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.model.Accessor;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatAuthenticationResolver implements HandlerMethodArgumentResolver {

    private final TokenExtractor tokenExtractor;
    private final TokenProvider tokenProvider;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(ChatAuth.class);
    }

    @Override
    public Accessor resolveArgument(MethodParameter parameter, Message<?> message) throws Exception {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        String extractedToken = tokenExtractor.extractToken(accessor.getFirstNativeHeader("Authorization"));

        tokenProvider.verify(extractedToken);

        String userId = tokenProvider.getSubject(extractedToken);

        return Accessor.member(Long.valueOf(userId));
    }
}
