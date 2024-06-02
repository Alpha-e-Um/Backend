package com.example.eumserver.domain.application.team.entity;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.global.dto.TimeStamp;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_announcement_id")
    private TeamAnnouncement announcement;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, orphanRemoval = true)
    private Resume resume;

    @Enumerated(EnumType.STRING)
    private TeamApplicationState state;

    @Embedded
    private TimeStamp timeStamp;

    public void cancel() {
        this.state = TeamApplicationState.WITHDRAWN;
    }

    public void accept() {
        this.state = TeamApplicationState.ACCEPTED;
    }

    public void reject() {
        this.state = TeamApplicationState.REJECTED;
    }
}
