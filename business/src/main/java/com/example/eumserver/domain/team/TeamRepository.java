package com.example.eumserver.domain.team;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {

    @Query("select t from Team t join t.participants p where p.participantId.userId = :userId")
    List<Team> findAllByUserId(@Param("userId") long userId);

}
