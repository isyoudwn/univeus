package com.example.univeus.common;

import com.example.univeus.domain.member.model.Member;
import com.example.univeus.domain.member.model.QMember;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class QueryDslTest {

    @Autowired
    EntityManager em;


    @Test
    void queryDsl_테스트() {
        // given
        Member member = new Member(
                null,
                "testEmail",
                null,
                null,
                null,
                null,
                null,
                null
        );

        em.persist(member);
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember qMember = QMember.member;

        Member findMember = query.selectFrom(qMember).fetchFirst();

        Assertions.assertThat(findMember).isEqualTo(member);
    }
}
