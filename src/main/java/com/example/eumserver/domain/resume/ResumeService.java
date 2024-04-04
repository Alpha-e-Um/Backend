package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.CustomException;
import com.example.eumserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Resume postResume(ResumeRequest resumeRequest, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        Resume resume = ResumeMapper.INSTANCE.resumeRequestToResume(resumeRequest);
        resume.setUser(user);
        resumeRepository.save(resume);
        return resume;
    }

    @Transactional
    public Resume updateResume(ResumeRequest resumeRequest, long resumeId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new EntityNotFoundException("Resume not found."));

        if (resume.getUser().getId() != userId) throw new CustomException(403, "No Autorization");

        Resume updatedResume = ResumeMapper.INSTANCE.resumeRequestToResume(resumeRequest);
        resume.updateResume(updatedResume.getTitle(), updatedResume.getJobCategory(), updatedResume.getJobSubcategory(),
                updatedResume.getGpa(), updatedResume.getTotalScore(), updatedResume.getCareers(), updatedResume.getActivities(),
                updatedResume.getCertificates(), updatedResume.getProjects(), updatedResume.getHomepages());
        resumeRepository.save(resume);

        return resume;
    }

}
