package com.example.eumserver.domain.announcement.team.controller;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementFilter;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementRequest;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementUpdateRequest;
import com.example.eumserver.domain.announcement.team.mapper.TeamAnnouncementMapper;
import com.example.eumserver.domain.announcement.team.service.TeamAnnouncementService;
import com.example.eumserver.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/team-announcement")
@RequiredArgsConstructor
public class TeamAnnouncementController {

    private final TeamAnnouncementService announcementService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResult<TeamAnnouncementResponse>> createAnnouncement(
            @RequestBody TeamAnnouncementRequest announcementRequest
    ) {
        log.debug("announcement request: {}", announcementRequest);
        TeamAnnouncementResponse announcementResponse = announcementService.createAnnouncement(announcementRequest);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("팀 공고 생성 성공", announcementResponse));
    }

    @GetMapping("")
    public ResponseEntity<ApiResult<Page<TeamAnnouncementResponse>>> getAnnouncements(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size,
            @RequestBody TeamAnnouncementFilter announcementFilter
    ) {
        Page<TeamAnnouncementResponse> filteredAnnouncementsWithPaging = announcementService.getFilteredAnnouncementsWithPaging(page, size, announcementFilter);
        return ResponseEntity
                .ok(new ApiResult<>("팀 공고 필터링 및 페이징 조회 성공", filteredAnnouncementsWithPaging));
    }

    @GetMapping("/{announcementId}")
    public ResponseEntity<ApiResult<TeamAnnouncementResponse>> getAnnouncement(@PathVariable(name = "announcementId") Long announcementId) {
        TeamAnnouncement announcement = announcementService.findAnnouncementById(announcementId);
        TeamAnnouncementResponse announcementResponse = TeamAnnouncementMapper.INSTANCE.entityToResponse(announcement);
        return ResponseEntity
                .ok(new ApiResult<>("팀 공고 조회 성공", announcementResponse));
    }

    @PutMapping("/{announcementId}")
    public ResponseEntity<ApiResult<?>> updateAnnouncement(
            @PathVariable(name = "announcementId") Long announcementId,
            @RequestBody TeamAnnouncementUpdateRequest announcementUpdateRequest
    ) {
        announcementService.updateAnnouncement(announcementId, announcementUpdateRequest);
        return ResponseEntity
                .ok(new ApiResult<>("팀 공고 업데이트 성공"));
    }

    @DeleteMapping("/{announcementId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<ApiResult<?>> deleteAnnouncement(
            @PathVariable(name = "announcementId") Long announcementId
    ) {
        announcementService.deleteAnnouncement(announcementId);
        return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ApiResult<>("팀 공고 삭제 성공"));
    }
}
