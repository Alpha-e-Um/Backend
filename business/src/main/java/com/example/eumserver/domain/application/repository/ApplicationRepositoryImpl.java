package com.example.eumserver.domain.application.repository;

import com.example.eumserver.domain.announcement.team.domain.QTeamAnnouncement;
import com.example.eumserver.domain.application.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.entity.ApplicationState;
import com.example.eumserver.domain.application.entity.QTeamApplication;
import com.example.eumserver.domain.application.entity.TeamApplication;
import com.example.eumserver.domain.application.mapper.ApplicationMapper;
import com.example.eumserver.domain.resume.entity.QResume;
import com.example.eumserver.domain.team.QTeam;
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
    public Page<MyApplicationResponse> getMyApplicationsWithPaging(Long userId, ApplicationState state,
                                                                   Pageable pageable) {
        QTeamApplication application = QTeamApplication.teamApplication;
        QTeamAnnouncement announcement = QTeamAnnouncement.teamAnnouncement;

        BooleanExpression predicate = application.isNotNull();

        predicate = predicate.and(application.user.id.eq(userId));

        if (state != ApplicationState.ALL) {
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

    @Override
    public boolean checkApplicationExist(Long userId, Long announcementId) {
        QTeamApplication application = QTeamApplication.teamApplication;

        BooleanExpression predicate = application.isNotNull();
        predicate = predicate.and(application.user.id.eq(userId));
        predicate = predicate.and(application.announcement.id.eq(announcementId));
        predicate = predicate.and(application.state.ne(ApplicationState.WITHDRAWN));

        long count = jpaQueryFactory
                .select(application)
                .from(application)
                .where(predicate)
                .fetch()
                .size();

        if(count == 0) return false;

        return true;
    }
}
