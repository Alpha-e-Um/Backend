package com.example.eumserver.domain.announcement.resume.controller;

import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementRequest;
import com.example.eumserver.domain.announcement.resume.service.ResumeAnnouncementService;
import com.example.eumserver.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/resume/{resumeId}/announcement")
@RequiredArgsConstructor
public class ResumeAnnouncementController {

    private final ResumeAnnouncementService resumeAnnouncementService;

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<ApiResult<?>> createResumeAnnouncement(
            @PathVariable Long resumeId,
            @RequestBody ResumeAnnouncementRequest request) {
        resumeAnnouncementService.publishResume(resumeId, request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("이력서 공고 생성"));
    }

    @DeleteMapping("/{resumeAnnouncementId}")
    public ResponseEntity<ApiResult<?>> deleteResumeAnnouncement(
            @PathVariable Long resumeId,
            @PathVariable Long resumeAnnouncementId) {
        resumeAnnouncementService.unpublishResume(resumeId, resumeAnnouncementId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiResult<>("이력서 공고 생성"));
    }
}
