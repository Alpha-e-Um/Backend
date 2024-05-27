package com.example.eumserver.domain.announcement.resume.repository;

import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementFilter;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ResumeAnnouncementCustomRepository {

    Page<ResumeAnnouncementResponse> findResumeAnnouncementsWithFilteredAndPagination(ResumeAnnouncementFilter filter, Pageable pageable);
}
