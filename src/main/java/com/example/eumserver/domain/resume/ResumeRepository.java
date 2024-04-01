package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
}
