package com.example.eumserver.domain.resume.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ResumeProjectRequest(
        @NotBlank String title,
        @NotBlank String projectRole,
        @NotBlank LocalDate startDate,
        LocalDate endDate,
        String projectUrl,
        String introduction
) {
}
