package com.example.eumserver.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "resume_projects")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_project_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(name = "project_title", nullable = false, length = 30)
    private String title;

    @Column(name = "project_role", nullable = false, length = 20)
    private String projectRole;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "project_url", length = 255)
    private String projectUrl;

    @Column(columnDefinition = "TEXT")
    private String introduction;

}
