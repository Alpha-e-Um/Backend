package com.example.eumserver.domain.team.announcement.domain;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementUpdateRequest;
import com.example.eumserver.global.dto.TimeStamp;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "team_announcements")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TeamAnnouncement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_announcement_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private int vacancies;

    @Lob
    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(name = "date_expired", nullable = false)
    private LocalDateTime expiredDate;

    @Setter
    @Column(name = "date_published")
    private LocalDateTime publishedDate;

    @Embedded
    private TimeStamp timeStamp;

    @ElementCollection(targetClass = OccupationClassification.class)
    @Enumerated(EnumType.STRING)
    private List<OccupationClassification> occupationClassifications;

    public void setTeam(Team team) {
        this.team = team;
        team.addAnnouncement(this);
    }

    public boolean isPublished() {
        return this.publishedDate != null;
    }

    public void updateAnnouncement(TeamAnnouncementUpdateRequest announcementUpdateRequest) {
        this.title = announcementUpdateRequest.title();
        this.description = announcementUpdateRequest.description();
        this.vacancies = announcementUpdateRequest.vacancies();
        this.expiredDate = announcementUpdateRequest.expiredDate();
        this.occupationClassifications = announcementUpdateRequest.occupationClassifications();
        if (!isPublished() && announcementUpdateRequest.publish()) {
            this.publishedDate = LocalDateTime.now();
        } else if (!announcementUpdateRequest.publish()) {
            this.publishedDate = null;
        }
    }
}
