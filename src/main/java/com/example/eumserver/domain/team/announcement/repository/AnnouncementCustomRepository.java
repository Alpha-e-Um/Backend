package com.example.eumserver.domain.team.announcement.repository;

import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnnouncementCustomRepository {

    /**
     * Retrieve a paginated list of announcements filtered additional criteria.
     *
     * @param filter   An object containing filter criteria to apply to the announcements.
     * @param pageable Pagination information including page number, page size, and sort order.
     * @return A {@link Page} of {@link Announcement} objects that match the specified filters.
     */
    Page<AnnouncementResponse> getFilteredAnnouncementsWithPaging(AnnouncementFilter filter, Pageable pageable);
}
