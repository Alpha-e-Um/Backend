package com.example.eumserver.domain.announcement.resume.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;

import java.util.List;

public record ResumeAnnouncementFilter(
        List<OccupationClassification> occupationClassifications
) {
}
