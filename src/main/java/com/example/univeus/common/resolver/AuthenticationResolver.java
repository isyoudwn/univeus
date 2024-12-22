package com.example.univeus.common.resolver;


import static com.example.univeus.common.exception.ErrorCode.*;

import com.example.univeus.common.annotation.Auth;
import com.example.univeus.domain.auth.RefreshTokenExtractor;
import com.example.univeus.domain.auth.TokenExtractor;
import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.exception.AuthException;
import com.example.univeus.domain.auth.exception.TokenException;
import com.example.univeus.domain.auth.model.Accessor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
@RequiredArgsConstructor
public class AuthenticationResolver implements HandlerMethodArgumentResolver {

    private final TokenProvider tokenProvider;
    private final RefreshTokenExtractor refreshTokenExtractor;
    private final TokenExtractor tokenExtractor;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Auth.class);
    }

    @Override
    public Accessor resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
                                    NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);

        if (request == null) {
            throw new AuthException(AUTH_BAD_REQUEST);
        }

        try {
            String refreshTokenValue = refreshTokenExtractor.extractToken(request.getCookies());
            String accessTokenValue = tokenExtractor.extractToken(request.getHeader("Authorization"));

            // 토큰을 검증에서 expired 되면 throw 한다
            tokenProvider.verify(refreshTokenValue);
            tokenProvider.verify(accessTokenValue);

            Long memberId = Long.valueOf(tokenProvider.getSubject(accessTokenValue));
            return Accessor.member(memberId);

        } catch (TokenException e) {
            return Accessor.guest();
        }
    }
}
