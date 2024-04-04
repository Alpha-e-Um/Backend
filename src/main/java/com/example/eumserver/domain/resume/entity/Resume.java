package com.example.eumserver.domain.resume.entity;

import com.example.eumserver.domain.user.User;
import com.example.eumserver.global.entity.TimeStamp;
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

    @Column(columnDefinition = "TEXT", nullable = false)
    private String introduction;

    @Builder.Default
    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;

    @Embedded
    private TimeStamp timeStamp;

    public void setUser(User user) {
        this.user = user;
        user.addResume(this);
    }

    public void updateResume(String title, String jobCategory, String jobSubcategory, Double gpa, Double totalScore,
                             List<ResumeCareer> careers, List<ResumeActivity> activities, List<ResumeCertificate> certificates,
                             List<ResumeProject> projects, List<ResumeHomepage> homepages, Boolean isPublic) {
        this.title = title;
        this.jobCategory = jobCategory;
        this.jobSubcategory = jobSubcategory;
        this.gpa = gpa;
        this.totalScore = totalScore;
        this.careers.clear();
        this.careers.addAll(careers);
        this.activities.clear();
        this.activities.addAll(activities);
        this.certificates.clear();
        this.certificates.addAll(certificates);
        this.projects.clear();
        this.projects.addAll(projects);
        this.homepages.clear();
        this.homepages.addAll(homepages);
        this.isPublic = isPublic;
    }
}
