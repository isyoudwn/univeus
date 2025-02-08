package com.example.univeus.domain.participant.repository;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.participant.model.Participant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByMeetingPost(MeetingPost meetingPost);

    Optional<Participant> findByMeetingPostAndMember(MeetingPost meetingPost, Member member);

//    @Modifying
//    @Query("DELETE FROM Participant p WHERE p.meetingPost = :meetingPost AND p.member = :member")
//    int deleteByMeetingPostAndMember(@Param("meetingPost") MeetingPost meetingPost, @Param("member") Member member);
}
