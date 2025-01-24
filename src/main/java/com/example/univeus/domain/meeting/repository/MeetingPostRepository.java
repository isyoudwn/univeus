package com.example.univeus.domain.meeting.repository;

import com.example.univeus.domain.meeting.model.MeetingPost;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingPostRepository extends JpaRepository<MeetingPost, Long> {

    @Query("SELECT p FROM MeetingPost p WHERE p.id < :id ORDER BY p.id DESC")
    List<MeetingPost> findAllById(@Param("id") Long id, Pageable pageable);

    @Query("SELECT p FROM MeetingPost p ORDER BY p.createdAt DESC, p.id DESC")
    List<MeetingPost> findAllByNoneCursor(Pageable pageable);

    Page<MeetingPost> findAllByOrderByIdDesc(Pageable pageable);
}
