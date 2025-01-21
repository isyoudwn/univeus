package com.example.univeus.domain.participant.service;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.participant.ParticipantRole;

public interface ParticipantService {

    void participate(MeetingPost meetingPost, Member member, ParticipantRole participantRole);
}
