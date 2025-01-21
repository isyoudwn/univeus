package com.example.univeus.domain.participant.repository;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.participant.Participant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
    List<Participant> findByMeetingPost(MeetingPost meetingPost);
}
