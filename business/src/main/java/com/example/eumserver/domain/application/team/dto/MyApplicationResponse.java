package com.example.eumserver.domain.application.team.dto;

import java.time.LocalDateTime;

public record MyApplicationResponse(
        String title,
        String state,
        LocalDateTime timestamp
) {
}
