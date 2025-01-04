package com.example.univeus.domain.meeting.repository;

import com.example.univeus.domain.meeting.model.MeetingPost;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MeetingPostRepository extends JpaRepository<MeetingPost, Long> {
}
