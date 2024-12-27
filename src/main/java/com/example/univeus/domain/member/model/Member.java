package com.example.univeus.domain.member.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(uniqueConstraints = {
        @UniqueConstraint(name = "member_email_unique", columnNames = {"email"}),
        @UniqueConstraint(name = "member_nickname_unique", columnNames = {"nickname"}),
        @UniqueConstraint(name = "member_student_id_unique", columnNames = {"studentId"}),
        @UniqueConstraint(name = "member_phone_number_unique", columnNames = {"phoneNumber"})
})
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false, length = 30)
    private String studentId;

    @Column(nullable = false, length = 30)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private Membership membership;

    @Enumerated(EnumType.STRING)
    private Department department;

    public static Member create(String email, String nickname, String studentId, String phoneNumber,
                                Gender gender, Membership membership, Department department) {
        return Member.builder()
                .email(email)
                .nickname(nickname)
                .studentId(studentId)
                .phoneNumber(phoneNumber)
                .gender(gender)
                .membership(membership)
                .department(department)
                .build();
    }
}
