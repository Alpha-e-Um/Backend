package com.example.eumserver.domain.announcement.team.repository;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.domain.announcement.team.domain.QTeamAnnouncement;
import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.announcement.team.mapper.TeamAnnouncementMapper;
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
public class TeamAnnouncementCustomRepositoryImpl implements TeamAnnouncementCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(TeamAnnouncementFilter filter, Pageable pageable) {
        QTeamAnnouncement teamAnnouncement = QTeamAnnouncement.teamAnnouncement;
        BooleanExpression predicate = teamAnnouncement.isNotNull();
        predicate = predicate.and(teamAnnouncement.publishedDate.isNotNull());

        List<OccupationClassification> occupationClassifications = filter.getOccupationClassifications();
        if (occupationClassifications != null && !occupationClassifications.isEmpty()) {
            predicate = predicate.and(teamAnnouncement.occupationClassifications.any().in(occupationClassifications));
        }

        List<TeamAnnouncement> announcements = queryFactory
                .selectFrom(teamAnnouncement)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<TeamAnnouncementResponse> announcementResponses = announcements.stream()
                .map(TeamAnnouncementMapper.INSTANCE::entityToResponse).toList();

        JPAQuery<Long> count = queryFactory
                .select(teamAnnouncement.count())
                .from(teamAnnouncement)
                .where(predicate);

        return PageableExecutionUtils.getPage(announcementResponses, pageable, count::fetchOne);
    }

    @Override
    public void updateViews(Long announcementId, Long views) {
        QTeamAnnouncement announcement = QTeamAnnouncement.teamAnnouncement;

        queryFactory.update(announcement)
                .set(announcement.views, views)
                .where(announcement.id.eq(announcementId))
                .execute();
    }
}
