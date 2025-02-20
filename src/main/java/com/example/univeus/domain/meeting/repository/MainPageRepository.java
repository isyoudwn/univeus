package com.example.univeus.domain.meeting.repository;

import com.example.univeus.domain.meeting.model.MeetingPost;
import com.example.univeus.domain.meeting.repository.custom.MainPageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MainPageRepository extends JpaRepository<MeetingPost, Long>, MainPageRepositoryCustom {
}
