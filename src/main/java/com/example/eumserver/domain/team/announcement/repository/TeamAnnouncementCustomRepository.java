package com.example.eumserver.domain.team.announcement.repository;

import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.team.announcement.domain.TeamAnnouncement;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamAnnouncementCustomRepository {

    /**
     * Retrieve a paginated list of announcements filtered additional criteria.
     *
     * @param filter   An object containing filter criteria to apply to the announcements.
     * @param pageable Pagination information including page number, page size, and sort order.
     * @return A {@link Page} of {@link TeamAnnouncement} objects that match the specified filters.
     */
    Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(Long teamId, TeamAnnouncementFilter filter, Pageable pageable);

}
