package com.example.univeus.domain.auth.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.example.univeus.domain.auth.model.RefreshToken;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;
    private RefreshToken fixureRefreshToken;

    @BeforeEach
    void setUp() {
        Member fixureMember = Member.create(
                "testEmail",
                "testNickname",
                "testStudentId",
                "testPhoneNumber",
                Gender.MAN,
                Membership.NORMAL,
                Department.ART_AND_PHYSICS
        );

        fixureRefreshToken = RefreshToken.create("testToken", fixureMember);
    }

    @Test
    void 조회_기능을_테스트한다() {
        // given
        RefreshToken createdRefreshToken = refreshTokenRepository.save(fixureRefreshToken);

        // when
        RefreshToken findRefreshToken = refreshTokenRepository.findById(fixureRefreshToken.getTokenValue())
                .orElse(null);

        // then
        assertEquals(createdRefreshToken.getTokenValue(), findRefreshToken.getTokenValue());
        assertEquals(createdRefreshToken.getMember(), findRefreshToken.getMember());
    }
}
