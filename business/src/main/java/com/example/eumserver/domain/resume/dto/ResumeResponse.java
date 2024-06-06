package com.example.eumserver.domain.resume.dto;

import com.example.eumserver.domain.resume.entity.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ResumeResponse {
    private Long id;
    private String title;
    private String description;
    private String jobCategory;
    private String jobSubcategory;
    private Double gpa;
    private Double totalScore;
    private List<String> techStacks;
    private List<ResumeCareer> careers;
    private List<ResumeActivity> activities;
    private List<ResumeCertificate> certificates;
    private List<ResumeProject> projects;
    private List<ResumeHomepage> homepages;
    private String createDate;
    private String updateAt;
}
