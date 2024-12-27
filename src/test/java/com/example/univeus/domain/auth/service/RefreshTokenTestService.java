package com.example.univeus.domain.auth.service;

import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import org.springframework.http.ResponseCookie;

public class RefreshTokenTestService implements RefreshTokenService {
    @Override
    public Boolean isExist(String id) {
        return !id.equals("notExist");
    }

    @Override
    public ResponseCookie createResponseToken(String tokenValue) {
        return ResponseCookie.from("test-token", tokenValue)
                .maxAge(60)
                .secure(true)
                .httpOnly(true)
                .sameSite("none")
                .path("/")
                .build();
    }

    @Override
    public RefreshToken save(String tokenValue, Member member) {
        return RefreshToken.create(tokenValue, member);
    }

    @Override
    public RefreshToken findById(String refreshToken) {
        Member member = new Member(
                1L,
                "testEmail",
                "nickName",
                "studentId",
                "phoneNumber",
                Gender.MAN,
                Membership.NORMAL,
                Department.ART_AND_PHYSICS
        );
        return RefreshToken.create(refreshToken, member);
    }

    @Override
    public void delete(RefreshToken refreshToken) {

    }
}
