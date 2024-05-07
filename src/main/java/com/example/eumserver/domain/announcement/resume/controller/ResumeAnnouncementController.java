package com.example.eumserver.domain.announcement.resume.controller;

import com.example.eumserver.domain.announcement.resume.domain.ResumeAnnouncement;
import com.example.eumserver.domain.announcement.resume.service.ResumeAnnouncementService;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.global.dto.ApiResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/resume/{resumeId}/announcement")
@RequiredArgsConstructor
public class ResumeAnnouncementController {

    private final ResumeAnnouncementService resumeAnnouncementService;

    @PostMapping("")
    public ResponseEntity<ApiResult<>>
}
