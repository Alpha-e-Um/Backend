package com.example.eumserver.domain.application.team.repository;

import com.example.eumserver.domain.application.team.entity.TeamApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<TeamApplication, Long>, ApplicationCustomRepository {

}
