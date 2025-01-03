package com.example.univeus.domain.auth.service;


import com.example.univeus.domain.auth.GoogleServerLogin.Response;
import com.example.univeus.domain.auth.SocialLogin;
import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.dto.UserTokens;
import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.auth.dto.response.AuthResponse.ResponseTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final RefreshTokenService refreshTokenService;
    private final TokenProvider tokenProvider;
    private final MemberService memberService;
    private final SocialLogin socialLogin;

    @Override
    public ResponseTokens reissueTokens(String tokenValue) {
        RefreshToken refreshToken = refreshTokenService.findById(tokenValue);
        Member member = refreshToken.getMember();
        refreshTokenService.delete(refreshToken);

        return issueTokens(member.getId());
    }


    @Override
    public ResponseTokens issueTokens(Long memberId) {
        UserTokens userTokens = tokenProvider.generateTokens(memberId.toString());
        Member member = memberService.findById(memberId);

        refreshTokenService.save(userTokens.refreshToken(), member);
        ResponseCookie refreshToken = refreshTokenService.createResponseToken(userTokens.refreshToken());

        return ResponseTokens.of(userTokens.accessToken(), refreshToken);
    }

    @Override
    public ResponseTokens createOrLogin(String googleIdToken) {
        Response googleProfile = socialLogin.getGoogleProfile(googleIdToken);
        String email = googleProfile.email();
        Member member = memberService.createOrFindMemberByEmail(email);

        member.checkProceed();

        return issueTokens(member.getId());
    }
}
