package com.example.eumserver.domain.resume.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ResumeActivityRequest(
        @NotBlank String title,
        @NotBlank LocalDate startDate,
        LocalDate endDate,
        String activityUrl,
        String introduction
) {
}
