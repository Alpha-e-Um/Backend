package com.example.eumserver.domain.team.announcement.dto;

import com.example.eumserver.domain.team.announcement.domain.OccupationClassification;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

public record AnnouncementUpdateRequest(
        String title,
        String description,
        int vacancies,
        List<OccupationClassification> occupationClassifications,
        boolean publish,

        @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime expiredDate
) {
}
