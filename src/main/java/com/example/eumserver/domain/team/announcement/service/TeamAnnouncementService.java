package com.example.eumserver.domain.team.announcement.service;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamService;
import com.example.eumserver.domain.team.announcement.domain.TeamAnnouncement;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementUpdateRequest;
import com.example.eumserver.domain.team.announcement.mapper.TeamAnnouncementMapper;
import com.example.eumserver.domain.team.announcement.repository.TeamAnnouncementRepository;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamAnnouncementService {

    private final TeamAnnouncementRepository announcementRepository;

    private final TeamService teamService;

    public Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(
            Long teamId,
            int page,
            TeamAnnouncementFilter filter
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("date_created"));
        Pageable pageable = PageRequest.of(page, 12, Sort.by(sorts));
        return announcementRepository.getFilteredAnnouncementsWithPaging(teamId, filter, pageable);
    }

    @Transactional
    public TeamAnnouncementResponse createAnnouncement(Long teamId, TeamAnnouncementRequest announcementRequest) {
        Team team = teamService.findById(teamId);

        TeamAnnouncement announcement = TeamAnnouncementMapper.INSTANCE.requestToEntity(announcementRequest);
        announcement.setTeam(team);
        if (announcementRequest.publish()) {
            LocalDateTime localDateTime = LocalDateTime.now();
            announcement.setPublishedDate(localDateTime);
        }

        announcementRepository.save(announcement);
        return TeamAnnouncementMapper.INSTANCE.entityToResponse(announcement);
    }

    @Transactional
    public void updateAnnouncement(Long announcementId, TeamAnnouncementUpdateRequest announcementUpdateRequest) {
        TeamAnnouncement announcement = this.findAnnouncementById(announcementId);
        announcement.updateAnnouncement(announcementUpdateRequest);
        announcementRepository.save(announcement);
    }


    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        TeamAnnouncement announcement = this.findAnnouncementById(announcementId);
        announcementRepository.delete(announcement);
    }

    public TeamAnnouncement findAnnouncementById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.ANNOUNCEMENT_NOT_FOUND));
    }
}