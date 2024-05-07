package com.example.eumserver.domain.announcement.resume.repository;

import com.example.eumserver.domain.announcement.resume.domain.ResumeAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeAnnouncementRepository extends JpaRepository<ResumeAnnouncement, Long> { }
