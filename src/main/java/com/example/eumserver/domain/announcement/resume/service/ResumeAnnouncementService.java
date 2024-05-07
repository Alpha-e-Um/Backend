package com.example.eumserver.domain.announcement.resume.service;

import com.example.eumserver.domain.announcement.resume.mapper.ResumeAnnouncementMapper;
import com.example.eumserver.domain.announcement.resume.repository.ResumeAnnouncementRepository;
import com.example.eumserver.domain.resume.ResumeRepository;
import com.example.eumserver.domain.announcement.resume.domain.ResumeAnnouncement;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeAnnouncementService {

    private final ResumeRepository resumeRepository;
    private final ResumeAnnouncementRepository resumeAnnouncementRepository;

    public void publishResume(Long resumeId, ResumeAnnouncementRequest resumeAnnouncementRequest) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        ResumeAnnouncement resumeAnnouncement = ResumeAnnouncementMapper.INSTANCE
                .requestToEntity(resumeAnnouncementRequest);

        resume.publishResume(resumeAnnouncement);
        resumeRepository.save(resume);
        resumeAnnouncementRepository.save(resumeAnnouncement);
    }

    public void unpublishResume(Long resumeId, Long resumeAnnouncementId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));
        ResumeAnnouncement resumeAnnouncement = resumeAnnouncementRepository.findById(resumeAnnouncementId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_ANNOUNCEMENT_NOT_FOUND));

        resume.unpublishResume();
        resumeRepository.save(resume);
        resumeAnnouncementRepository.delete(resumeAnnouncement);
    }

}
