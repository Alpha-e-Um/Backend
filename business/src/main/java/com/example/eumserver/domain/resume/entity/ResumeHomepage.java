package com.example.eumserver.domain.resume.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    private Resume resume;
    
    @Column(name = "homepage_url", length = 255, nullable = false)
    private String homepageUrl;

    @Column(length = 255)
    private String description;

    public void setResume(Resume resume) {
        this.resume = resume;
    }
}
