package com.example.eumserver.domain.application.repository;


import com.example.eumserver.domain.application.entity.TeamApplication;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<TeamApplication, Long>, ApplicationCustomRepository {

}
