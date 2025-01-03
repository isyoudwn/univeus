package com.example.univeus.domain.auth.service;


import static com.example.univeus.common.response.ResponseMessage.*;

import com.example.univeus.domain.auth.GoogleServerLogin.Response;
import com.example.univeus.domain.auth.SocialLogin;
import com.example.univeus.domain.auth.TokenProvider;
import com.example.univeus.domain.auth.dto.UserTokens;
import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.exception.MemberException;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Nickname;
import com.example.univeus.presentation.auth.dto.request.AuthRequest.Profile;
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

    @Override
    public void registerProfile(Long memberId, Profile profileRequest) {
        Department department = Department.of(profileRequest.department());
        Gender gender = Gender.of(profileRequest.gender());

        memberService.updateProfile(memberId, department, gender, profileRequest.nickname(),
                profileRequest.studentId());
    }

    @Override
    public void checkNicknameDuplicated(Nickname nicknameRequest) {
        String nickname = nicknameRequest.nickname();
        if (memberService.findByNickname(nickname).isPresent()) {
            throw new MemberException(MEMBER_NICKNAME_DUPLICATED);
        }
    }
}
