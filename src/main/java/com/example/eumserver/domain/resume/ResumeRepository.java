package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.announcement.resume.repository.ResumeAnnouncementCustomRepository;
import com.example.eumserver.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeAnnouncementCustomRepository {
    Optional<Resume> findById(Long resumeId);
    List<Resume> findByUserId(Long userId);
}
