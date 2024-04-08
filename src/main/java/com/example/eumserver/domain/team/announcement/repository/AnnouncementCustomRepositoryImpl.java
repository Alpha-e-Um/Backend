package com.example.eumserver.domain.team.announcement.repository;

import com.example.eumserver.domain.team.announcement.domain.QAnnouncement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AnnouncementCustomRepositoryImpl implements AnnouncementCustomRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Announcement> getFilteredAnnouncementsWithPaging(AnnouncementFilter filter, Pageable pageable) {
        QAnnouncement announcement = QAnnouncement.announcement;
        // TODO: paging 구현
        return null;
    }
}
