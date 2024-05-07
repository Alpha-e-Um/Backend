package com.example.eumserver.domain.resume.announcement.repository;

import com.example.eumserver.domain.resume.announcement.domain.ResumeAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResumeAnnouncementRepository extends JpaRepository<ResumeAnnouncement, Long> { }
