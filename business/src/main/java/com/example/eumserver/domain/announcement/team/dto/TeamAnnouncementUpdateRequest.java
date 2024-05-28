package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record TeamAnnouncementUpdateRequest(
        String title,
        String region,
        String description,
        int vacancies,
        List<OccupationClassification> occupationClassifications,
        boolean publish,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expiredDate
) {
}
