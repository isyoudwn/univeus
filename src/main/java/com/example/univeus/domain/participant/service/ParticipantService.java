package com.example.univeus.domain.participant.service;


public interface ParticipantService {

    void participate(Long postId, Long memberId);

    void removeParticipant(Long postId, Long memberId);
}
