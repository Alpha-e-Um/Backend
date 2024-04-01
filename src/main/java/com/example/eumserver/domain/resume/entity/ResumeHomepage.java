package com.example.eumserver.domain.resume.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume_websites")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResumeHomepage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_activity_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    @Column(nullable = false)
    private String type;

    @Column(name = "homepage_url", length = 255, nullable = false)
    private String homepageUrl;
}
