package com.example.eumserver.domain.application.controller;

import com.example.eumserver.domain.application.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.entity.ApplicationState;
import com.example.eumserver.domain.application.service.ApplicationService;
import com.example.eumserver.domain.jwt.PrincipalDetails;
import com.example.eumserver.global.dto.ApiResult;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
    public ResponseEntity<ApiResult<Page<MyApplicationResponse>>> getMyApplication (@AuthenticationPrincipal
    PrincipalDetails details){
        Page<MyApplicationResponse> applications = applicationService.getMyApplications(details.getUserId(), ApplicationState.ALL);
        return ResponseEntity.ok(new ApiResult<>("내 지원현황 받아오기 성공", applications));
    }

//    @PostMapping("{announcement_id}")
//    public ResponseEntity<ApiResult<?>> applyTeamAnnouncement(@AuthenticationPrincipal PrincipalDetails details,
//                                                              @PathVariable(name = "announcement_id") long announcement,
//                                                              @RequestParam(name = "resume_id", required = false) long resumeId){
//
//
//    }
}
