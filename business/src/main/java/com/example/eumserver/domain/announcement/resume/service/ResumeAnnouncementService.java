package com.example.eumserver.domain.announcement.resume.service;

import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementFilter;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementResponse;
import com.example.eumserver.domain.announcement.resume.mapper.ResumeAnnouncementMapper;
import com.example.eumserver.domain.announcement.resume.repository.ResumeAnnouncementRepository;
import com.example.eumserver.domain.resume.repository.ResumeRepository;
import com.example.eumserver.domain.announcement.resume.domain.ResumeAnnouncement;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeAnnouncementService {

    private final ResumeRepository resumeRepository;
    private final ResumeAnnouncementRepository resumeAnnouncementRepository;

    public void publishResume(ResumeAnnouncementRequest resumeAnnouncementRequest) {
        Resume resume = resumeRepository.findById(resumeAnnouncementRequest.resumeId())
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        ResumeAnnouncement resumeAnnouncement = ResumeAnnouncementMapper.INSTANCE
                .requestToEntity(resumeAnnouncementRequest);
        resumeAnnouncement.setResume(resume);
        resumeAnnouncementRepository.save(resumeAnnouncement);
    }

    public void unpublishResume(Long resumeAnnouncementId) {
        ResumeAnnouncement resumeAnnouncement = resumeAnnouncementRepository.findById(resumeAnnouncementId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_ANNOUNCEMENT_NOT_FOUND));
        resumeAnnouncementRepository.delete(resumeAnnouncement);
    }

    public Page<ResumeAnnouncementResponse> getResumeAnnouncements(ResumeAnnouncementFilter filter) {
        if (filter.getSize() <= 0) {
            throw new CustomException("사이즈를 1 이상으로 설정해주세요. 현재: " + filter.getSize(), ErrorCode.INVALID_INPUT_VALUE);
        }

        if (filter.getPage() < 0) {
            throw new CustomException("페이지는 음수여선 안됩니다. 현재: " + filter.getPage(), ErrorCode.INVALID_INPUT_VALUE);
        }

        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("date_created"));
        Pageable pageable = PageRequest.of(filter.getPage(), filter.getSize(), Sort.by(sorts));
        return resumeRepository.findResumeAnnouncementsWithFilteredAndPagination(filter, pageable);
    }
}
