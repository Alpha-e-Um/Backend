package com.example.eumserver.domain.application.dto;

import java.time.LocalDateTime;

public record MyApplicationResponse(
        String title,
        String state,
        LocalDateTime timestamp
) {
}
