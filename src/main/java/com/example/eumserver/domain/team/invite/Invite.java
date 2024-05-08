package com.example.eumserver.domain.team.invite;

import com.example.eumserver.domain.team.Team;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "invites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Invite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(nullable = false, name = "date_expired")
    private Date expiredDate;

    public Invite(Team team, String token, Date expiredDate) {
        this.team = team;
        this.token = token;
        this.expiredDate = expiredDate;
    }

}
