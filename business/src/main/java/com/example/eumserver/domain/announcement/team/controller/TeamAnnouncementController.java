package com.example.eumserver.domain.announcement.team.controller;

import com.example.eumserver.domain.announcement.team.dto.*;
import com.example.eumserver.domain.announcement.team.service.TeamAnnouncementService;
import com.example.eumserver.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "팀원 구하기 공고")
@Slf4j
@RestController
@RequestMapping("/api/team-announcement")
@RequiredArgsConstructor
public class TeamAnnouncementController {

    private final TeamAnnouncementService announcementService;

    @PostMapping("")
    @Operation(summary = "팀원 구하기 공고 올리기", description = "팀원 구하기 공고를 생성합니다.")
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

    /**
     * 팀 공고를 필터에 따라 조회합니다.
     * 어노테이션이 적용되어 있지 않지만, 쿼리 파라미터로 들어가게 됩니다.
     *
     * @param filter 팀 공고 필터
     * @return 페이징이 적용된 팀 공고 리스트
     */
    @GetMapping("")
    @Operation(summary = "팀원 구하기 공고를 필터에 따라 조회", description = "만료 여부, 검색 필터, 글 목록 필터 등에 따라 페이지네이션으로 보여줍니다.")
    public ResponseEntity<ApiResult<TeamAnnouncementResponseWrapper>> getAnnouncements(
            TeamAnnouncementFilter filter
    ) {
        TeamAnnouncementResponseWrapper filteredAnnouncementsWithPaging = announcementService.getFilteredAnnouncementsWithPaging(filter);
        return ResponseEntity
                .ok(new ApiResult<>("팀 공고 필터링 및 페이징 조회 성공", filteredAnnouncementsWithPaging));
    }

    @GetMapping("/{announcementId}")
    @Operation(summary = "팀원 구하기 공고 세부 조회", description = "팀원 구하기 공고의 세부 사항을 보여줍니다.")
    public ResponseEntity<ApiResult<TeamAnnouncementDetailResponse>> getAnnouncement(
            @RequestHeader(name = "Authorization", required = false) String authorization,
            @PathVariable(name = "announcementId") Long announcementId) {
        TeamAnnouncementDetailResponse announcement = announcementService.viewPost(announcementId, authorization);
        return ResponseEntity
                .ok(new ApiResult<>("팀 공고 조회 성공", announcement));
    }

    //TODO : 팀원 구하기 공고 수정 및 삭제가 Token을 받지 않고 진행되는데, 왜 이렇게 한 것인지 궁금합니다.
    @PutMapping("/{announcementId}")
    @Operation(summary = "팀원 구하기 공고 수정", description = "팀원 구하기 공고의 세부 사항을 보여줍니다.")
    public ResponseEntity<ApiResult<?>> updateAnnouncement(
            @PathVariable(name = "announcementId") Long announcementId,
            @RequestBody TeamAnnouncementUpdateRequest announcementUpdateRequest
    ) {
        announcementService.updateAnnouncement(announcementId, announcementUpdateRequest);
        return ResponseEntity
                .ok(new ApiResult<>("팀 공고 업데이트 성공"));
    }

    @DeleteMapping("/{announcementId}")
    @Operation(summary = "팀원 구하기 공고 삭제", description = "팀원 구하기 공고를 삭제합니다.")
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
