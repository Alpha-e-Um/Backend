package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserId(Long userId);

    List<Resume> findByUserIdAndIsPublicTrue(Long userId);
}
