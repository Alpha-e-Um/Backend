package com.example.eumserver.domain.team.participant;

import com.example.eumserver.domain.team.Team;
import com.example.eumserver.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import static com.example.eumserver.domain.team.participant.ParticipantId.COLUMN_PARTICIPANT_TEAM_ID;
import static com.example.eumserver.domain.team.participant.ParticipantId.COLUMN_PARTICIPANT_USER_ID;

@Entity
@Table(name = "participants")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Participant {

    @EmbeddedId
    private ParticipantId participantId;

    private ParticipantRole role;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("teamId")
    @JoinColumn(name = COLUMN_PARTICIPANT_TEAM_ID)
    private Team team;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = COLUMN_PARTICIPANT_USER_ID)
    private User user;

    @Builder
    public Participant(Team team, User user) {
        setTeam(team);
        setUser(user);
    }

    public void setTeam(Team team) {
        this.team = team;
        team.addTeam(this);
    }

    public void setUser(User user) {
        this.user = user;
        user.addTeam(this);
    }
}
