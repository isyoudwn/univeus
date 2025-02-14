package com.example.univeus.domain.participant.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.service.MeetingPostService;
import com.example.univeus.domain.member.model.Department;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.Membership;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.domain.participant.repository.ParticipantRepository;
import com.example.univeus.presentation.meeting.dto.request.MeetingPostRequest.MeetingPostContent;
import com.example.univeus.presentation.meeting.dto.request.MeetingPostRequest.MeetingPostImagesUris;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ParticipantServiceImplTest {
    @Autowired
    private ParticipantService participantService;

    @Autowired
    private MeetingPostService meetingPostService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private ParticipantRepository participantRepository;

    private ExecutorService executorService;
    private CountDownLatch latch;


    @BeforeEach
    void setUp() {
        executorService = Executors.newFixedThreadPool(5); // 5개의 스레드 풀 생성
        latch = new CountDownLatch(5); // 5개의 작업 완료 대기
    }

    @Test
    void 참가자_5명이_동시에_접근할_경우_동시성_이슈가_발생한다() throws InterruptedException {
        // given
        Member member1 = createMember("user1@example.com", "UserOne", "2021001", "010-1234-5678", Gender.WOMAN);
        Member member2 = createMember("user2@example.com", "UserTwo", "2021002", "010-9876-5432", Gender.WOMAN);
        Member member3 = createMember("user3@example.com", "UserThree", "2021003", "010-5555-5555", Gender.MAN);
        Member member4 = createMember("user4@example.com", "UserFour", "1231312", "010-1232-5555", Gender.MAN);
        Member member5 = createMember("user5@example.com", "UserFive", "13213123", "010-5555-2342", Gender.MAN);
        Member member6 = createMember("user6@example.com", "UserSix", "1121212", "010-5555-2333", Gender.MAN);

        MeetingPost meetingPost = createMeetingPost();

        for (Member member : Arrays.asList(member2, member3, member4, member5, member6)) {
            executorService.submit(() -> {
                try {
                    participantService.participate(meetingPost.getId(), member.getId());
                }
                catch (Exception e) {
                    System.out.println(e.getMessage());
                }
                finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(participantRepository.findByMeetingPost(meetingPost).size()).isEqualTo(3); // 참가 제한은 3명이어야 함
    }

    public Member createMember(String email, String name, String studentId, String phone, Gender gender) {
        Member member = Member.create(email, name, studentId, phone, gender, Membership.PRO,
                Department.ART_AND_PHYSICS);
        memberService.createMember(member);
        return member;
    }

    public MeetingPost createMeetingPost() {
        return meetingPostService.writePost(
                1L,
                new MeetingPostContent(
                        "스터디 모집합니다!",
                        "자바 스터디 함께 하실 분 모집합니다. 초보자 환영!",
                        "NONE",
                        "3",
                        "스터디모집",
                        "2026-02-20T18:00",
                        "2026-02-25T15:00",
                        "서울시 강남구 테헤란로 123",
                        "37.498095",
                        "127.027610"
                ), new MeetingPostImagesUris(
                        List.of("uri")
                ));
    }
}
