package com.example.eumserver.domain.announcement.team.dto;

import java.time.LocalDateTime;

public record TeamAnnouncementResponse(
        Long id,
        String title,
        String description,
        String region,
        LocalDateTime createDate,

        Long teamId,
        String teamLogo
) {
}
