package com.example.univeus.domain.auth.service;


import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.dto.UserTokens;
import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;

    @Override
    public ResponseTokens reissueTokens(String tokenValue) {
        RefreshToken refreshToken = refreshTokenService.findById(tokenValue);
        Member member = refreshToken.getMember();
        refreshTokenService.delete(refreshToken);

        return issueTokens(member.getId());
    }


    public ResponseTokens issueTokens(Long memberId) {
        UserTokens userTokens = tokenProvider.generateTokens(memberId.toString());
        Member member = memberService.findById(memberId);

        refreshTokenService.save(userTokens.refreshToken(), member);
        ResponseCookie refreshToken = refreshTokenService.createResponseToken(userTokens.refreshToken());

        return ResponseTokens.of(userTokens.accessToken(), refreshToken);
    }
}
