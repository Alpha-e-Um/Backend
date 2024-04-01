package com.example.eumserver.domain.resume.dto;

import java.util.List;

public record ResumeRequest(
        String jobCategory,
        String jobSubcategory,
        Double gpa,
        Double totalScore,
        String introduction,
        List<ResumeCareerRequest> careers,
        List<ResumeCertificateRequest>certificates,
        List<ResumeProjectRequest> projects)
{
    //TODO : Resume 직군이 제대로 들어갔는지 ValidCheck
}
