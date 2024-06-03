package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.global.domain.Region;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TeamAnnouncementUpdateRequest(
        String title,
        Region region,
        String description,
        String summary,
        int vacancies,
        List<OccupationClassification> occupationClassifications,
        boolean publish,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expiredDate
) {
}
