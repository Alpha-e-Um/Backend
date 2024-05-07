package com.example.eumserver.domain.resume.announcement.service;

import com.example.eumserver.domain.resume.ResumeRepository;
import com.example.eumserver.domain.resume.announcement.domain.ResumeAnnouncement;
import com.example.eumserver.domain.resume.announcement.dto.ResumeAnnouncementRequest;
import com.example.eumserver.domain.resume.announcement.mapper.ResumeAnnouncementMapper;
import com.example.eumserver.domain.resume.announcement.repository.ResumeAnnouncementRepository;
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

    public ResumeAnnouncement publishResume(Long resumeId, ResumeAnnouncementRequest resumeAnnouncementRequest) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        ResumeAnnouncement resumeAnnouncement = ResumeAnnouncementMapper.INSTANCE
                .requestToEntity(resumeAnnouncementRequest);

        resume.publishResume(resumeAnnouncement);
        resumeRepository.save(resume);
        return resumeAnnouncementRepository.save(resumeAnnouncement);
    }

    public void unpublishResume(Long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));
        ResumeAnnouncement resumeAnnouncement = resume.getResumeAnnouncement();

        resume.unpublishResume();
        resumeRepository.save(resume);
        resumeAnnouncementRepository.delete(resumeAnnouncement);
    }

}
