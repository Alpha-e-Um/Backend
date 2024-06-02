package com.example.eumserver.domain.application.team.dto;

import java.time.LocalDateTime;

public record MyTeamApplicationResponse(
        String state,
        LocalDateTime createDate
) {
}
