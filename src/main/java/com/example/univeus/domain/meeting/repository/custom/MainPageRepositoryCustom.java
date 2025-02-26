package com.example.univeus.domain.meeting.repository.custom;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MainPageRepositoryCustom {
    List<MeetingPost> findByCategoryAndOptionalCursor(MeetingCategory category, Long cursorId);

    Page<MeetingPost> findByCategoryAndOffset(MeetingCategory category, Pageable pageable);
}
