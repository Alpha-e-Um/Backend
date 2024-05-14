package com.example.eumserver.domain.application.repository;

import com.example.eumserver.domain.application.entity.Application;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ApplicationRepository extends JpaRepository<Application, Long>, ApplicationCustomRepository {

}
