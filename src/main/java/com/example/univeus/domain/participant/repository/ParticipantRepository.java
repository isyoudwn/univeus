package com.example.univeus.domain.participant.repository;

import com.example.univeus.domain.participant.Participant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRepository extends JpaRepository<Participant, Long> {
}
