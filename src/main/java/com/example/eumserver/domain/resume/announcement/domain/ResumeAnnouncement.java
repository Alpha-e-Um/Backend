package com.example.eumserver.domain.resume.announcement.domain;

import com.example.eumserver.domain.resume.entity.Resume;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "resume_announcements")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ResumeAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "resume_announcement_id")
    private Long id;

    @Column(nullable = false)
    private String introduction;

    @Setter
    @OneToOne(mappedBy = "resumeAnnouncement")
    private Resume resume;
}
