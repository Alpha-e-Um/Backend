package com.example.eumserver.domain.announcement.resume.domain;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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

    @ElementCollection(targetClass = OccupationClassification.class)
    @Enumerated(EnumType.STRING)
    private List<OccupationClassification> occupationClassifications;

    @Setter
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "resume_id")
    private Resume resume;

    @Embedded
    private TimeStamp timeStamp;
}
