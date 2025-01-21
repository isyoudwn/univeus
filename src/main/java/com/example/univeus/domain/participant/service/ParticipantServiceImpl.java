package com.example.univeus.domain.participant.service;

import static com.example.univeus.common.response.ResponseMessage.ALREADY_PARTICIPANT;
import static com.example.univeus.common.response.ResponseMessage.PARTICIPANT_EXCEEDED;
import static com.example.univeus.common.response.ResponseMessage.PARTICIPANT_GENDER_LIMIT;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.service.MeetingPostService;
import com.example.univeus.domain.member.model.Gender;
import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.service.MemberService;
import com.example.univeus.domain.participant.Participant;
import com.example.univeus.domain.participant.ParticipantRole;
import com.example.univeus.domain.participant.exception.ParticipantException;
import com.example.univeus.domain.participant.repository.ParticipantRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ParticipantServiceImpl implements ParticipantService {

    private final ParticipantRepository participantRepository;
    private final MeetingPostService meetingPostService;
    private final MemberService memberService;

    @Override
    @Transactional
    public void participate(Long postId, Long memberId) {
        MeetingPost meetingPost = meetingPostService.findById(postId);
        List<Participant> participants = participantRepository.findByMeetingPost(meetingPost);

        if (meetingPost.getJoinLimit() < participants.size() + 1) {
            throw new ParticipantException(PARTICIPANT_EXCEEDED);
        }

        Member member = memberService.findById(memberId);

        if (meetingPost.getGenderLimit() != Gender.NONE && meetingPost.getGenderLimit() != member.getGender()) {
            throw new ParticipantException(PARTICIPANT_GENDER_LIMIT);
        }

        if (isAlreadyParticipate(participants, member)) {
            throw new ParticipantException(ALREADY_PARTICIPANT);
        }

        Participant participant = Participant.create(member, meetingPost, ParticipantRole.PARTICIPANT);
        participantRepository.save(participant);
    }

    @Transactional
    public Boolean isAlreadyParticipate(List<Participant> participants, Member member) {
        return participants.stream()
                .map(participant -> participant.getMember().equals(member))
                .findFirst().isPresent();
    }
}
