package com.example.eumserver.domain.team.participant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ParticipantRepository extends JpaRepository<Participant, ParticipantId> {

    @Query("select p from Participant p join User u on p.user.id = u.id where p.team.id = :teamId")
    List<Participant> findAllByTeamId(long teamId);
}
