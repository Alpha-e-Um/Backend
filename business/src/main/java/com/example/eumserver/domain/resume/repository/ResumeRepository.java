package com.example.eumserver.domain.resume.repository;

import com.example.eumserver.domain.announcement.resume.repository.ResumeAnnouncementCustomRepository;
import com.example.eumserver.domain.resume.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long>, ResumeAnnouncementCustomRepository {
    Optional<Resume> findByIdAndTimeStampIsDeletedFalse(Long resumeId);
    List<Resume> findByUserIdAndTimeStampIsDeletedFalse(Long userId);
}
