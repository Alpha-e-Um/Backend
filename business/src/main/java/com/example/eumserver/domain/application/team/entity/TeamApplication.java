package com.example.eumserver.domain.application.team.entity;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "team_applications")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TeamApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_application_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_announcement_id")
    private TeamAnnouncement announcement;

    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    private Resume resume;

    @Column
    private String state;

    @Embedded
    private TimeStamp timeStamp;
}
