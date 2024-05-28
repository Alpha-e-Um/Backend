package com.example.eumserver.domain.resume.dto;

import com.example.eumserver.domain.resume.entity.HomepageType;
import jakarta.validation.constraints.NotBlank;

public record ResumeHomepageRequest(
        @NotBlank HomepageType homepageType,
        String homepageUrl
) {
}
