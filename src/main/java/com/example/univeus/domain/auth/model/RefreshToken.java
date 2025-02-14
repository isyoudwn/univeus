package com.example.univeus.domain.auth.model;

import com.example.univeus.domain.member.model.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    private String tokenValue;

    @ManyToOne
    private Member member;

    public static RefreshToken create(String tokenValue, Member member) {
        return RefreshToken
                .builder()
                .tokenValue(tokenValue)
                .member(member)
                .build();
    }
}
