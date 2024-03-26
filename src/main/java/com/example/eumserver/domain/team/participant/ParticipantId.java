package com.example.eumserver.domain.team.participant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ParticipantId implements Serializable {

    public static final String COLUMN_PARTICIPANT_USER_ID = "participant_user_id";
    public static final String COLUMN_PARTICIPANT_TEAM_ID = "participant_team_id";

    @Column(name = COLUMN_PARTICIPANT_TEAM_ID)
    private Long teamId;

    @Column(name = COLUMN_PARTICIPANT_USER_ID)
    private Long userId;

}
