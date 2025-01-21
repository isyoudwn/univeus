package com.example.univeus.domain.participant.service;

import static com.example.univeus.common.response.ResponseMessage.ALREADY_PARTICIPANT;
import static com.example.univeus.common.response.ResponseMessage.CANT_PARTICIPATE_AFTER_THE_DEADLINE;
import static com.example.univeus.common.response.ResponseMessage.NOT_PARTICIPATE_THIS_MEETING;
import static com.example.univeus.common.response.ResponseMessage.OWNER_CANT_LEAVE_THE_MEETING;
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
import java.time.Clock;
import java.time.LocalDateTime;
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
    private final Clock clock;

    @Override
    @Transactional
    public void participate(Long postId, Long memberId) {
        MeetingPost meetingPost = meetingPostService.findById(postId);
        List<Participant> participants = participantRepository.findByMeetingPost(meetingPost);

        if (meetingPost.getJoinLimit() < participants.size() + 1) {
            throw new ParticipantException(PARTICIPANT_EXCEEDED);
        }

        if (meetingPost.getPostDeadLine().getPostDeadline().isAfter(LocalDateTime.now(clock))) {
            throw new ParticipantException(CANT_PARTICIPATE_AFTER_THE_DEADLINE);
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

    @Override
    @Transactional
    public void removeParticipant(Long postId, Long memberId) {
        MeetingPost meetingPost = meetingPostService.findById(postId);
        Member member = memberService.findById(memberId);

        Participant participant = participantRepository.findByMeetingPostAndMember(meetingPost, member)
                .orElseThrow(() -> new ParticipantException(NOT_PARTICIPATE_THIS_MEETING));

        if (participant.getParticipantRole() == ParticipantRole.OWNER) {
            throw new ParticipantException(OWNER_CANT_LEAVE_THE_MEETING);
        }

        participantRepository.delete(participant);
    }

    @Transactional
    public Boolean isAlreadyParticipate(List<Participant> participants, Member member) {
        return participants.stream()
                .anyMatch(participant -> participant.getMember().equals(member));
    }
}
