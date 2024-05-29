package com.example.eumserver.domain.announcement.team.service;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamService;
import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementRequest;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementUpdateRequest;
import com.example.eumserver.domain.announcement.team.mapper.TeamAnnouncementMapper;
import com.example.eumserver.domain.announcement.team.repository.TeamAnnouncementRepository;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
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

    private final RedisTemplate<String, Object> redisTemplate;

    public Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(
            TeamAnnouncementFilter filter
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("date_created"));
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(sorts));
        return announcementRepository.getFilteredAnnouncementsWithPaging(filter, pageable);
    }

    @Transactional
    public TeamAnnouncementResponse createAnnouncement(TeamAnnouncementRequest announcementRequest) {
        Team team = teamService.findById(announcementRequest.teamId());

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
        TeamAnnouncement announcement = announcementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_ANNOUNCEMENT_NOT_FOUND));
        announcementRepository.delete(announcement);
    }

    public TeamAnnouncement findAnnouncementById(Long announcementId, String token) {
        if(token != null){
            addView(2);
        }

        return announcementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_ANNOUNCEMENT_NOT_FOUND));
    }

    private void addView(Long userId){

        SetOperations<String, Object> set = redisTemplate.opsForSet();
        set.add("viewUser", userId);

    }

}
