package com.example.univeus.domain.meeting;

import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Member writer;

    @Column(length = 50)
    private String title;

    @Column(length = 1500)
    private String body;

    @OneToOne
    private PostImage thumbNail;

    @Enumerated(EnumType.STRING)
    private Gender genderLimit;

    @Embedded
    private Location location;

    @Embedded
    private PostDeadLine postDeadLine;

    @Embedded
    private MeetingDateTime meetingDateTime;

    @Enumerated(EnumType.STRING)
    private PostCategory postCategory;
}
