package com.example.eumserver.domain.resume.entity;

import com.example.eumserver.domain.user.User;
import com.example.eumserver.global.dto.TimeStamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "resumes")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Resume {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    @Column(name = "job_category", nullable = false)
    private String jobCategory;

    @Column(name = "job_subcategory", nullable = false)
    private String jobSubcategory;

    @Column
    private Double gpa;

    @Column(name = "total_score")
    private Double totalScore;

    @Builder.Default
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeCareer> careers = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeActivity> activities = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeCertificate> certificates = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeProject> projects = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeHomepage> homepages = new ArrayList<>();


    @Embedded
    private TimeStamp timeStamp;

    public void setUser(User user) {
        this.user = user;
        user.addResume(this);
    }

    public void updateResume(Resume resume) {
        this.title = resume.getTitle();
        this.description = resume.getDescription();
        this.jobCategory = resume.getJobCategory();
        this.jobSubcategory = resume.getJobSubcategory();
        this.gpa = resume.getGpa();
        this.totalScore = resume.getTotalScore();
        this.careers.clear();
        this.careers.addAll(resume.getCareers());
        this.activities.clear();
        this.activities.addAll(resume.getActivities());
        this.certificates.clear();
        this.certificates.addAll(resume.getCertificates());
        this.projects.clear();
        this.projects.addAll(resume.getProjects());
        this.homepages.clear();
        this.homepages.addAll(resume.getHomepages());
    }

}
