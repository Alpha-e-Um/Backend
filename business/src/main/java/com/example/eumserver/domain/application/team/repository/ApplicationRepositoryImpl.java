package com.example.eumserver.domain.application.team.repository;

import com.example.eumserver.domain.announcement.team.domain.QTeamAnnouncement;
import com.example.eumserver.domain.application.team.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.team.entity.QTeamApplication;
import com.example.eumserver.domain.application.team.entity.TeamApplication;
import com.example.eumserver.domain.application.team.mapper.ApplicationMapper;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ApplicationRepositoryImpl implements ApplicationCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MyApplicationResponse> getMyApplicationsWithPaging(Long user_id, String state,
                                                                   Pageable pageable) {
        QTeamApplication application = QTeamApplication.teamApplication;
        QTeamAnnouncement announcement = QTeamAnnouncement.teamAnnouncement;

        BooleanExpression predicate = application.isNotNull();

        predicate = predicate.and(application.user.id.eq(user_id));

        if (!state.equals("all")) {
            predicate = predicate.and(application.state.eq(state));
        }

        List<TeamApplication> applications = jpaQueryFactory
                .select(application)
                .from(application)
                .leftJoin(application.announcement, announcement).fetchJoin()
                .where(predicate)
                .fetch();

        List<MyApplicationResponse> myApplicationResponses = applications.stream()
                .map(ApplicationMapper.INSTANCE::entityToMyApplicationResponse).toList();

        JPAQuery<Long> count = jpaQueryFactory
                .select(application.count())
                .from(application)
                .where(predicate);

        return PageableExecutionUtils.getPage(myApplicationResponses, pageable, count::fetchOne);
    }
}
