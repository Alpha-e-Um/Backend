package com.example.eumserver.domain.announcement.team.service;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.*;
import com.example.eumserver.domain.announcement.team.mapper.TeamAnnouncementMapper;
import com.example.eumserver.domain.announcement.team.repository.TeamAnnouncementRepository;
import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamService;
import com.example.eumserver.domain.post.PostService;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import com.example.eumserver.global.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class TeamAnnouncementService extends PostService<TeamAnnouncementRepository, TeamAnnouncement> {

    private final TeamService teamService;
    private final String prefix = "teamannouncement";

    // Token 추출 때문에 임시로 추가했습니다. 추후, Gateway추가하면 제거예정
    private final JwtTokenProvider jwtTokenProvider;

    public TeamAnnouncementService(RedisUtil redisUtil, TeamAnnouncementRepository announcementRepository, TeamService teamService, JwtTokenProvider jwtTokenProvider) {
        super(redisUtil, announcementRepository);
        this.teamService = teamService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public Page<TeamAnnouncementResponse> getFilteredAnnouncementsWithPaging(
            TeamAnnouncementFilter filter
    ) {
        if (filter.getSize() <= 0) {
            throw new CustomException("사이즈를 1 이상으로 설정해주세요. 현재: " + filter.getSize(), ErrorCode.INVALID_INPUT_VALUE);
        }

        if (filter.getPage() < 0) {
            throw new CustomException("페이지는 음수여선 안됩니다. 현재: " + filter.getPage(), ErrorCode.INVALID_INPUT_VALUE);
        }
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("date_created"));
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(sorts));
        return postRepository.getFilteredAnnouncementsWithPaging(filter, pageable);
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

        postRepository.save(announcement);
        return TeamAnnouncementMapper.INSTANCE.entityToResponse(announcement);
    }

    @Transactional
    public void updateAnnouncement(Long announcementId, TeamAnnouncementUpdateRequest announcementUpdateRequest) {
        TeamAnnouncement announcement = this.findPostById(announcementId);
        announcement.updateAnnouncement(announcementUpdateRequest);
        postRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        TeamAnnouncement announcement = this.findPostById(announcementId);
        postRepository.delete(announcement);
    }

    @Transactional
    public TeamAnnouncementDetailResponse viewPost(Long announcementId, String authorization) {
        TeamAnnouncement announcement = this.findPostById(announcementId);

        if (authorization != null) {
//            String token = jwtTokenProvider.resolveAccessToken(authorization);
//            String userId = jwtTokenProvider.parseClaims(token).getSubject();
//            System.out.println(userId);
            increaseViewCount(announcementId, String.valueOf(1), announcement.getViews());
        }

        TeamAnnouncementDetailResponse response = TeamAnnouncementMapper.INSTANCE.entityToDetailResponse(announcement);

        String views = (String) redisUtil.getValues(getPrefix() + ":view::" + announcementId);
        System.out.println(views);
        if (views == null) response.setViews(announcement.getViews());
        else response.setViews(Long.parseLong(views));

        return response;
    }

    public TeamAnnouncement findPostById(Long announcementId) {
        return postRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_ANNOUNCEMENT_NOT_FOUND));
    }

    public String getPrefix(){
        return this.prefix;
    }
}
