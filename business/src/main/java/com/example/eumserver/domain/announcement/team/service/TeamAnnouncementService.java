package com.example.eumserver.domain.announcement.team.service;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.*;
import com.example.eumserver.domain.announcement.team.mapper.TeamAnnouncementMapper;
import com.example.eumserver.domain.announcement.team.repository.TeamAnnouncementRepository;
import com.example.eumserver.domain.jwt.JwtTokenProvider;
import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.TeamService;
import com.example.eumserver.domain.view.ViewService;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import com.example.eumserver.global.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@Slf4j
public class TeamAnnouncementService extends ViewService<TeamAnnouncementRepository> {

    private final TeamService teamService;

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
        TeamAnnouncement announcement = this.findAnnouncementById(announcementId);
        announcement.updateAnnouncement(announcementUpdateRequest);
        postRepository.save(announcement);
    }

    @Transactional
    public void deleteAnnouncement(Long announcementId) {
        TeamAnnouncement announcement = this.findAnnouncementById(announcementId);
        postRepository.delete(announcement);
    }

    @Transactional
    public TeamAnnouncementDetailResponse viewAnnouncement(Long announcementId, String authorization) {
        if (authorization != null) {
            String token = jwtTokenProvider.resolveAccessToken(authorization);
            String userId = jwtTokenProvider.parseClaims(token).getSubject();
            increaseViewCount(announcementId, userId);
        }

        TeamAnnouncement announcement = this.findAnnouncementById(announcementId);
        TeamAnnouncementDetailResponse response = TeamAnnouncementMapper.INSTANCE.entityToDetailResponse(announcement);

        Long view = (Long) redisUtil.getValues("announcement:view:" + announcementId);
        if (view == null) response.setViews(announcement.getViews());
        else response.setViews(view);

        return response;
    }

    public TeamAnnouncement findAnnouncementById(Long announcementId) {
        return postRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_ANNOUNCEMENT_NOT_FOUND));
    }

    public void increaseViewCount(Long announcementId, String userId) {
        String key = "announcement:view::" + announcementId;
        String userKey = "announcement:user::" + announcementId;

        while (!redisUtil.lock("lock::" + key)) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        try {
            if (redisUtil.hasKey(key) && redisUtil.hasKey(userKey)) {
                if (redisUtil.checkValuesSet(userKey, userId)) {
                    redisUtil.increaseValue(key);
                    return;
                } else return;
            } else {
                TeamAnnouncement announcement = this.findAnnouncementById(announcementId);
                redisUtil.setValue(key, announcement.getViews(),
                        Duration.ofSeconds(480).toMillis(), TimeUnit.SECONDS);
                redisUtil.createValueSet(userKey, Duration.ofSeconds(480).toMillis(), TimeUnit.SECONDS);
            }
        } finally {
            redisUtil.unlock("lock::" + key);
        }
    }

    /**
     * 3분에 한번씩 실제 DB에 조회수를 반영합니다.
     * 글의 수가 많이질 수록 오래걸리는 작업이라 비동기처리 했습니다.
     * 이 부분은 추후 Batch로 빼는 것이 적합합니다.
     */
    @Async
    @Transactional
    @Scheduled(cron = "0 0/3 * * * *")
    protected void applyViewToDB() {
        Set<String> keys = redisUtil.keys("announcement:view:*");
        Iterator<String> iterator = keys.iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            Long announcementId = Long.parseLong(key.split("::")[1]);
            String views = (String) redisUtil.getValues(key);
            if (views != null) {
                announcementRepository.updateViews(announcementId, Long.valueOf(views));
            }
        }

        log.info("Views updated at {}", LocalDateTime.now());
    }
}
