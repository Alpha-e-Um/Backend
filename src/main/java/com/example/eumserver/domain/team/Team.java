package com.example.eumserver.domain.team;

import com.example.eumserver.domain.team.announcement.domain.TeamAnnouncement;
import com.example.eumserver.domain.team.participant.Participant;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "teams")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String email;

    @Column
    private String logo;

    @Column(name = "phone_number")
    private String phoneNumber;

    @CreationTimestamp
    @Column(name = "formation_date", nullable = false, updatable = false)
    private LocalDateTime formationDate;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Participant> participants = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    @OneToMany(mappedBy = "team", orphanRemoval = true)
    private List<TeamAnnouncement> announcements = new ArrayList<>();

    public void addTeam(Participant participant) {
        this.participants.add(participant);
    }

    public void addAnnouncement(TeamAnnouncement announcement) {
        this.announcements.add(announcement);
    }
}
