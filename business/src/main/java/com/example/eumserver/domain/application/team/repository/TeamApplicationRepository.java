package com.example.eumserver.domain.application.team.repository;


import com.example.eumserver.domain.application.team.entity.TeamApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamApplicationRepository extends JpaRepository<TeamApplication, Long>, TeamApplicationCustomRepository {
}
