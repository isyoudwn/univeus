package com.example.univeus.domain.meeting.repository.custom;

import static com.example.univeus.domain.meeting.model.QMeetingPost.meetingPost;

import com.example.univeus.domain.meeting.model.MeetingCategory;
import com.example.univeus.domain.meeting.model.MeetingPost;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MainPageRepositoryCustomImpl implements MainPageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<MeetingPost> findByCategoryAndOptionalCursor(MeetingCategory category, Long cursorId) {
        BooleanBuilder builder = new BooleanBuilder();

        if (category != null) {
            builder.and(meetingPost.meetingCategory.eq(category));
        }

        if (cursorId != null) {
            builder.and(meetingPost.id.lt(cursorId));
        }

        return queryFactory
                .selectFrom(meetingPost)
                .where(builder)
                .orderBy(meetingPost.id.desc())
                .limit(20)
                .fetch();
    }

    @Override
    public Page<MeetingPost> findByCategoryAndOffset(
            MeetingCategory category,
            Pageable pageable
    ) {
        BooleanBuilder builder = new BooleanBuilder();

        if (category != null) {
            builder.and(meetingPost.meetingCategory.eq(category));
        }

        List<MeetingPost> content = queryFactory
                .selectFrom(meetingPost)
                .where(builder)
                .orderBy(meetingPost.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        long total = queryFactory
                .select(meetingPost.count())
                .from(meetingPost)
                .where(builder)
                .stream().count();

        return new PageImpl<>(content, pageable, total);
    }
}
