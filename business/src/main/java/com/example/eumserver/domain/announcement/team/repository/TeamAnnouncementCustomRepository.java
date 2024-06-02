package com.example.eumserver.domain.announcement.team.repository;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.post.PostRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TeamAnnouncementCustomRepository extends PostRepository {

    /**
     * Retrieve a paginated list of announcements filtered additional criteria.
     *
     * @param filter   An object containing filter criteria to apply to the announcements.
     * @param pageable Pagination information including page number, page size, and sort order.
     * @return A {@link Page} of {@link TeamAnnouncement} objects that match the specified filters.
     */
    Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(TeamAnnouncementFilter filter, Pageable pageable);

    void updateViews(Long announcementId, Long views);
}
