package com.example.eumserver.domain.application.team.repository;

import com.example.eumserver.domain.announcement.team.domain.QTeamAnnouncement;
import com.example.eumserver.domain.application.team.dto.MyTeamApplicationResponse;
import com.example.eumserver.domain.application.team.entity.QTeamApplication;
import com.example.eumserver.domain.application.team.entity.TeamApplicationState;
import com.example.eumserver.domain.application.team.entity.TeamApplication;
import com.example.eumserver.domain.application.team.mapper.TeamApplicationMapper;
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
public class TeamApplicationRepositoryImpl implements TeamApplicationCustomRepository {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<MyTeamApplicationResponse> getMyApplicationsWithPaging(Long userId, TeamApplicationState state,
                                                                       Pageable pageable) {
        QTeamApplication application = QTeamApplication.teamApplication;
        QTeamAnnouncement announcement = QTeamAnnouncement.teamAnnouncement;

        BooleanExpression predicate = application.isNotNull();

        predicate = predicate.and(application.user.id.eq(userId));

        if (state != TeamApplicationState.ALL) {
            predicate = predicate.and(application.state.eq(state));
        }

        List<TeamApplication> applications = jpaQueryFactory
                .select(application)
                .from(application)
                .leftJoin(application.announcement, announcement).fetchJoin()
                .where(predicate)
                .fetch();

        List<MyTeamApplicationResponse> myTeamApplicationRespons = applications.stream()
                .map(TeamApplicationMapper.INSTANCE::entityToMyApplicationResponse).toList();

        JPAQuery<Long> count = jpaQueryFactory
                .select(application.count())
                .from(application)
                .where(predicate);

        return PageableExecutionUtils.getPage(myTeamApplicationRespons, pageable, count::fetchOne);
    }

    @Override
    public boolean checkApplicationExist(Long userId, Long announcementId) {
        QTeamApplication application = QTeamApplication.teamApplication;

        BooleanExpression predicate = application.isNotNull();
        predicate = predicate.and(application.user.id.eq(userId));
        predicate = predicate.and(application.announcement.id.eq(announcementId));
        predicate = predicate.and(application.state.ne(TeamApplicationState.WITHDRAWN));

        long count = jpaQueryFactory
                .select(application)
                .from(application)
                .where(predicate)
                .fetch()
                .size();

        return count != 0;
    }

    @Override
    public TeamApplication getApplicationWithState(Long userId, Long applicationId, TeamApplicationState state) {
        QTeamApplication application = QTeamApplication.teamApplication;

        BooleanExpression predicate = application.isNotNull();
        predicate = predicate.and(application.user.id.eq(userId));
        predicate = predicate.and(application.id.eq(applicationId));
        predicate = predicate.and(application.state.eq(state));

        return jpaQueryFactory
                .select(application)
                .from(application)
                .where(predicate)
                .fetchOne();
    }
}
