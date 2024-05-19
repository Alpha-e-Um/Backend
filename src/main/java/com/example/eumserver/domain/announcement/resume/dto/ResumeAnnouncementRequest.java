package com.example.eumserver.domain.announcement.resume.dto;

public record ResumeAnnouncementRequest(
        Long resumeId,
        String introduction
) {
}
