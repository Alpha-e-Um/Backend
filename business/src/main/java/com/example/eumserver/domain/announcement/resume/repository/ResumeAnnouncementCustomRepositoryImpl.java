package com.example.eumserver.domain.announcement.resume.repository;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.domain.announcement.resume.domain.QResumeAnnouncement;
import com.example.eumserver.domain.announcement.resume.domain.ResumeAnnouncement;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementFilter;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementResponse;
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
public class ResumeAnnouncementCustomRepositoryImpl implements ResumeAnnouncementCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ResumeAnnouncementResponse> findResumeAnnouncementsWithFilteredAndPagination(ResumeAnnouncementFilter filter, Pageable pageable) {
        QResumeAnnouncement resumeAnnouncement = QResumeAnnouncement.resumeAnnouncement;
        BooleanExpression predicate = resumeAnnouncement.isNotNull();

        List<OccupationClassification> occupationClassifications = filter.occupationClassifications();
        if (occupationClassifications != null && !occupationClassifications.isEmpty()) {
            predicate = predicate.and(resumeAnnouncement.occupationClassifications.any().in(occupationClassifications));
        }

        List<ResumeAnnouncement> announcements = queryFactory
                .select(resumeAnnouncement)
                .where(predicate)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<ResumeAnnouncementResponse> resumeAnnouncementResponses = announcements.stream()
                .map(announcement ->
                        new ResumeAnnouncementResponse(
                                announcement.getId(),
                                announcement.getResume().getId(),
                                announcement.getIntroduction(),
                                announcement.getOccupationClassifications())).toList();

        JPAQuery<Long> count = queryFactory
                .select(resumeAnnouncement.count())
                .from(resumeAnnouncement)
                .where(predicate);

        return PageableExecutionUtils.getPage(resumeAnnouncementResponses, pageable, count::fetchOne);
    }

}
