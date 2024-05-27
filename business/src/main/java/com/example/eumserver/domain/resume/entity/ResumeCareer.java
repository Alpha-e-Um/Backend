package com.example.eumserver.domain.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * 이력에서 자신의 경력을 담는 Entity
 * TODO : 회사에서 이용했던 기술들을 적는 technologiesUsed를 구현해야됨 (별도 테이블 생성?)
 */
@Entity
@Table(name = "resume_careers")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeCareer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_career_id")
    private Long id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Column(name = "company_name", nullable = false, length = 15)
    private String companyName;

    @Column(name = "company_role", nullable = false, length = 20)
    private String companyRole;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Builder.Default
    private String technologiesUsed = "Spring 장인";

    @Column(name = "company_website_url", length = 255)
    private String companyWebsiteUrl;

    @Column(columnDefinition = "TEXT")
    private String achievement;

    public void setResume(Resume resume) {
        this.resume = resume;
    }
}
