package com.example.eumserver.domain.announcement.team.repository;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.domain.announcement.team.domain.QTeamAnnouncement;
import com.example.eumserver.domain.announcement.team.domain.ScoredAnnouncement;
import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.announcement.team.mapper.TeamAnnouncementMapper;
import com.example.eumserver.domain.post.PostSortingOption;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TeamAnnouncementCustomRepositoryImpl implements TeamAnnouncementCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(TeamAnnouncementFilter filter, Pageable pageable) {
        QTeamAnnouncement teamAnnouncement = QTeamAnnouncement.teamAnnouncement;
        BooleanExpression predicate = teamAnnouncement.isNotNull();
        predicate = predicate.and(teamAnnouncement.publishedDate.isNotNull());

        if (filter.isExpired()) {
            predicate.and(teamAnnouncement.closed.eq(false));
        }

        List<OccupationClassification> occupationClassifications = filter.getOccupationClassifications();
        if (occupationClassifications != null && !occupationClassifications.isEmpty()) {
            predicate = predicate.and(teamAnnouncement.occupationClassifications.any().in(occupationClassifications));
        }

        JPAQuery<TeamAnnouncement> query = queryFactory
                .selectFrom(teamAnnouncement)
                .where(predicate);

        switch (filter.getOption()) {
            case LATEST:
                System.out.println("latest");
                query.orderBy(teamAnnouncement.timeStamp.createDate.desc());
                break;
            case VIEWS:
                query.orderBy(teamAnnouncement.views.desc());
                break;
            case POPULAR:
                query.where(teamAnnouncement.views.ne(0L));
                query.orderBy(teamAnnouncement.publishedDate.desc());
                break;
        }

        List<TeamAnnouncement> announcements = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<TeamAnnouncementResponse> announcementResponses;

        if (filter.getOption() != PostSortingOption.POPULAR) {
            announcementResponses = announcements.stream()
                    .map(TeamAnnouncementMapper.INSTANCE::entityToResponse).toList();
        } else {
            List<ScoredAnnouncement> popularAnnouncement = getPopularPostList(announcements);

            announcementResponses = popularAnnouncement.stream()
                    .map(scoredAnnouncement -> TeamAnnouncementMapper.INSTANCE.entityToResponse(scoredAnnouncement.getAnnouncement()))
                    .collect(Collectors.toList());

            announcementResponses = announcementResponses.stream()
                    .skip(pageable.getOffset())
                    .limit(pageable.getPageSize())
                    .collect(Collectors.toList());
        }

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

    @Override
    public ScoredAnnouncement createScoredPost(TeamAnnouncement post, double score) {
        return new ScoredAnnouncement(post, score);
    }
}
