package com.example.eumserver.domain.application.team.controller;

import com.example.eumserver.domain.application.team.dto.MyTeamApplicationFilterRequest;
import com.example.eumserver.domain.application.team.dto.MyTeamApplicationResponse;
import com.example.eumserver.domain.application.team.entity.TeamApplication;
import com.example.eumserver.domain.application.team.service.ApplicationService;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * 팀 모집
 */
@Tag(name = "팀 공고 지원")
@RestController
@RequestMapping("/api/application")
@RequiredArgsConstructor
public class TeamApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("")
    @Operation(summary = "내 지원현황 전부 받아오기", description = "state 필터에 따라서 나의 지원현황을 전부 받아오는 기능")
    public ResponseEntity<ApiResult<Page<MyTeamApplicationResponse>>> getMyApplication(
            @AuthenticationPrincipal PrincipalDetails details,
            MyTeamApplicationFilterRequest filterRequest
    ) {
        Page<MyTeamApplicationResponse> applications = applicationService.getMyApplications(details.getUserId()
                , filterRequest.getState(), filterRequest.getPage());
        return ResponseEntity.ok(new ApiResult<>("내 지원현황 받아오기 성공", applications));
    }

    @PostMapping("/{announcement_id}")
    @ApiResponse(responseCode = "201", description = "팀에 지원하기 성공")
    @Operation(summary = "팀 공고에 지원하기", description = "팀 공고에 지원하는 기능입니다. 이미 지원했다면, 지원이 되지 않습니다.")
    public ResponseEntity<ApiResult<TeamApplication>> applyTeamAnnouncement(@AuthenticationPrincipal PrincipalDetails details,
                                                                            @PathVariable(name = "announcement_id") Long announcementId,
                                                                            @RequestParam(name = "resume_id", required = false) Long resumeId) {
        TeamApplication application = applicationService.applyTeam(details.getUserId(), announcementId, resumeId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("팀에 지원하기 성공", application));
    }


    @PutMapping("/{application_id}/cancel")
    @ApiResponse(responseCode = "200", description = "팀에 지원하기 취소 성공")
    @Operation(summary = "팀 공고 지원 취소하기", description = "지원한 팀 공고에 지원 취소하는 기능입니다.")
    public ResponseEntity<ApiResult<TeamApplication>> cancelApplication(
            @AuthenticationPrincipal PrincipalDetails details,
            @PathVariable(name = "application_id") Long applicationId) {
        TeamApplication application = applicationService.cancelApplication(details.getUserId(), applicationId);
        return ResponseEntity
                .ok(new ApiResult<>("공고 지원하기 취소 성공", application));
    }

    @PutMapping("/{application_id}/accept")
    @ApiResponse(responseCode = "200", description = "팀 공고 지원 수락 성공")
    @Operation(summary = "팀 공고 지원 수락하기", description = "지원한 팀 공고에 대해 수락하는 기능입니다.")
    public ResponseEntity<ApiResult<TeamApplication>> acceptApplication(
            @AuthenticationPrincipal PrincipalDetails details,
            @PathVariable(name = "application_id") Long applicationId) {
        TeamApplication application = applicationService.acceptApplication(details.getUserId(), applicationId);
        return ResponseEntity
                .ok(new ApiResult<>("공고 지원 수락 성공", application));
    }

    @PutMapping("/{application_id}/reject")
    @ApiResponse(responseCode = "200", description = "팀 공고 지원 거절 성공")
    @Operation(summary = "팀 공고 지원 거절하기", description = "지원한 팀 공고에 대해 거절하는 기능입니다.")
    public ResponseEntity<ApiResult<TeamApplication>> rejectApplication(
            @AuthenticationPrincipal PrincipalDetails details,
            @PathVariable(name = "application_id") Long applicationId) {
        TeamApplication application = applicationService.rejectApplication(details.getUserId(), applicationId);
        return ResponseEntity
                .ok(new ApiResult<>("공고 지원 거절 성공", application));
    }
}