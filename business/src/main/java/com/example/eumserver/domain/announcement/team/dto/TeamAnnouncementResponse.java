package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.global.domain.Region;

import java.time.LocalDateTime;
import java.util.List;

public record TeamAnnouncementResponse(
        Long id,
        String title,
        String description,
        String summary,
        Region region,
        LocalDateTime createDate,
        LocalDateTime expiredDate,
        List<OccupationClassification> occupationClassifications,

        Long teamId,
        String teamName,
        String teamLogo
) {
}
