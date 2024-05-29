package com.example.eumserver.domain.announcement.resume.controller;

import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementFilter;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementRequest;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementResponse;
import com.example.eumserver.domain.announcement.resume.service.ResumeAnnouncementService;
import com.example.eumserver.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume-announcement")
@RequiredArgsConstructor
public class ResumeAnnouncementController {

    private final ResumeAnnouncementService resumeAnnouncementService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResult<?>> createResumeAnnouncement(
            @RequestBody ResumeAnnouncementRequest request) {
        resumeAnnouncementService.publishResume(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("이력서 공고 생성"));
    }

    @DeleteMapping("/{resumeAnnouncementId}")
    public ResponseEntity<ApiResult<?>> deleteResumeAnnouncement(
            @PathVariable Long resumeAnnouncementId) {
        resumeAnnouncementService.unpublishResume(resumeAnnouncementId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("이력서 공고 생성"));
    }

    @GetMapping("")
    public ResponseEntity<ApiResult<Page<ResumeAnnouncementResponse>>> getResumeAnnouncements(
            @RequestBody ResumeAnnouncementFilter filter,
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "12") int size
    ) {
        Page<ResumeAnnouncementResponse> resumeAnnouncementResponses = resumeAnnouncementService.getResumeAnnouncements(filter, page, size);
        return ResponseEntity.ok(new ApiResult<>("이력서 공고 필터링 및 페이징 조회 성공", resumeAnnouncementResponses));
    }
}
