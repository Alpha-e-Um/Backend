package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;

import java.time.LocalDateTime;
import java.util.List;

public record TeamAnnouncementResponse(
        Long id,
        String title,
        String description,
        String region,
        LocalDateTime createDate,
        List<OccupationClassification> occupationClassifications,

        Long teamId,
        String teamLogo
) {
}
