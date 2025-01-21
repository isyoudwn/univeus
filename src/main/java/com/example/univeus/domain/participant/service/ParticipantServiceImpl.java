package com.example.univeus.domain.participant.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.participant.Participant;
import com.example.univeus.domain.participant.ParticipantRole;
import com.example.univeus.domain.participant.repository.ParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;

    @Override
    @Transactional
    public void participate(MeetingPost meetingPost, Member member, ParticipantRole participantRole) {
        Participant participant  = Participant.create(member, meetingPost, participantRole);
        participant.addMeetingPost(meetingPost);
        participantRepository.save(participant);
    }
}
