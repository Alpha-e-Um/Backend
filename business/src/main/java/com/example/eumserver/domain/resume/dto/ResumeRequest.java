package com.example.eumserver.domain.resume.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record ResumeRequest(
        @NotBlank String title,
        @NotBlank String description,
        @NotBlank String jobCategory,
        @NotBlank String jobSubcategory,
        Double gpa,
        Double totalScore,
        Boolean isPublic,
        List<ResumeCareerRequest> careers,
        List<ResumeCertificateRequest> certificates,
        List<ResumeProjectRequest> projects,
        List<ResumeHomepageRequest> homepages,
        List<ResumeActivityRequest> activities
) {
    //TODO : Resume 직군이 제대로 들어갔는지 ValidCheck
}
