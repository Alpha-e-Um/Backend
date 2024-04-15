package com.example.eumserver.domain.team.announcement.repository;

import com.example.eumserver.domain.team.announcement.domain.OccupationClassification;
import com.example.eumserver.domain.team.announcement.domain.QAnnouncement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import com.example.eumserver.domain.team.announcement.mapper.AnnouncementMapper;
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
public class AnnouncementCustomRepositoryImpl implements AnnouncementCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AnnouncementResponse> getFilteredAnnouncementsWithPaging(Long teamId, AnnouncementFilter filter, Pageable pageable) {
        QAnnouncement announcement = QAnnouncement.announcement;
        BooleanExpression predicate = announcement.isNotNull();

        predicate = predicate.and(announcement.team.id.eq(teamId));

        if (filter.published()) {
            predicate = predicate.and(announcement.publishedDate.isNotNull());
        }

        List<OccupationClassification> occupationClassifications = filter.occupationClassifications();
        if (occupationClassifications != null && !occupationClassifications.isEmpty()) {
            predicate = predicate.and(announcement.occupationClassifications.any().in(occupationClassifications));
        }

        List<Announcement> announcements = queryFactory
                .selectFrom(announcement)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<AnnouncementResponse> announcementResponses = announcements.stream()
                .map(AnnouncementMapper.INSTANCE::entityToResponse).toList();

        JPAQuery<Long> count = queryFactory
                .select(announcement.count())
                .from(announcement)
                .where(predicate);

        return PageableExecutionUtils.getPage(announcementResponses, pageable, count::fetchOne);
    }
}
