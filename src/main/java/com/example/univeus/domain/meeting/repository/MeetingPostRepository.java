package com.example.univeus.domain.meeting.repository;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MeetingPostRepository extends JpaRepository<MeetingPost, Long> {

    // 패치 조인을 이용한 n+1문제 해결
    @Query("SELECT m FROM MeetingPost m " +
            "LEFT JOIN FETCH m.images " +
            "JOIN FETCH m.writer " +
            "WHERE m.id = :postId")
    Optional<MeetingPost> findByIdWithInfo(@Param("postId") Long postId);

    @Query("SELECT m FROM MeetingPost m " +
            "LEFT JOIN FETCH m.images " +
            "JOIN FETCH m.writer " +
            "WHERE m.id = :postId")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<MeetingPost> findByIdWithLock(@Param("postId") Long postId);

    @Deprecated // 커서기반 단일 최신순 조건 정렬
    @Query("SELECT p FROM MeetingPost p WHERE p.id < :id ORDER BY p.id DESC")
    List<MeetingPost> findAllById(@Param("id") Long id, Pageable pageable);

    @Deprecated
    @Query("SELECT p FROM MeetingPost p ORDER BY p.createdAt DESC, p.id DESC")
    List<MeetingPost> findAllByNoneCursor(Pageable pageable);

    @Deprecated
    Page<MeetingPost> findAllByOrderByIdDesc(Pageable pageable);

    @Query(
            "SELECT p FROM MeetingPost p " +
                    "WHERE (:category IS NULL OR p.meetingCategory = :category) " +
                    "ORDER BY p.id DESC"
    )
    Page<MeetingPost> findByCategoryAndOffset(
            @Param("category") MeetingCategory category,
            Pageable pageable
    );

    @Query(
            "SELECT p FROM MeetingPost p " +
                    "WHERE (:category IS NULL OR p.meetingCategory = :category) " +
                    "AND (:cursorId IS NULL OR p.id < :cursorId) " +
                    "ORDER BY p.id DESC"
    )
    List<MeetingPost> findByCategoryAndOptionalCursor(
            @Param("category") MeetingCategory category,
            @Param("cursorId") Long cursorId,
            Pageable pageable
    );
}
