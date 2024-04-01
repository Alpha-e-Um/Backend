package com.example.eumserver.domain.resume.entity;

import com.example.eumserver.domain.user.User;
import com.example.eumserver.global.entity.TimeStamp;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
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

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeCareer> careers = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeActivity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeCertificate> certificates = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ResumeProject> projects = new ArrayList<>();

    @Column(columnDefinition = "TEXT", nullable = false)
    private String introduction;

    @Embedded
    private TimeStamp timeStamp;

    public void setUser(User user) {
        this.user = user;
        user.addResume(this);
    }
}
