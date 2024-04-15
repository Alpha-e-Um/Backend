package com.example.eumserver.domain.team.announcement.service;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamService;
import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementFilter;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementUpdateRequest;
import com.example.eumserver.domain.team.announcement.mapper.AnnouncementMapper;
import com.example.eumserver.domain.team.announcement.repository.AnnouncementRepository;
import com.example.eumserver.global.error.CustomException;
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
public class AnnouncementService {

    private final AnnouncementRepository announcementRepository;

    private final TeamService teamService;

    public Page<AnnouncementResponse> getFilteredAnnouncementsWithPaging(
            AnnouncementFilter filter,
            int page
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("create_data"));
        Pageable pageable = PageRequest.of(page, 12, Sort.by(sorts));
        return announcementRepository.getFilteredAnnouncementsWithPaging(filter, pageable);
    }

    public AnnouncementResponse createAnnouncement(AnnouncementRequest announcementRequest) {
        Team team = teamService.findById(announcementRequest.teamId());

        Announcement announcement = AnnouncementMapper.INSTANCE.requestToEntity(announcementRequest);
        announcement.setTeam(team);
        if (announcementRequest.publish()) {
            LocalDateTime localDateTime = LocalDateTime.now();
            announcement.setPublishedDate(localDateTime);
        }

        announcementRepository.save(announcement);
        return AnnouncementMapper.INSTANCE.entityToResponse(announcement);
    }

    public void updateAnnouncement(AnnouncementUpdateRequest announcementUpdateRequest) {
        Announcement announcement = this.findAnnouncementById(announcementUpdateRequest.announcementId());
        announcement.updateAnnouncement(announcementUpdateRequest);
        announcementRepository.save(announcement);
    }


    public void deleteAnnouncement(Long announcementId) {
        Announcement announcement = this.findAnnouncementById(announcementId);
        announcementRepository.delete(announcement);
    }

    public Announcement findAnnouncementById(Long announcementId) {
        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(400, "Announcement not found."));
    }
}
