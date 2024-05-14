package com.example.eumserver.domain.application.entity;

import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "applications")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_announcement_id")
    private Announcement announcement;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    private Resume resume;

    @Column
    private String state;

    @Embedded
    private TimeStamp timeStamp;
}
