package com.example.eumserver.domain.resume.dto;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record ResumeCareerRequest(
        @NotBlank String companyName,
        @NotBlank String companyRole,
        @NotBlank LocalDate startDate,
        LocalDate endDate,
        String technologiesUsed,
        String companyWebsiteUrl,
        String achievement
) {
}
