package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResumeService {

    private final UserRepository userRepository;
    private final ResumeRepository resumeRepository;

    @Transactional
    public Resume postResume(ResumeRequest resumeRequest, long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Resume resume = ResumeMapper.INSTANCE.resumeRequestToResume(resumeRequest);
        resume.setUser(user);
        resumeRepository.save(resume);
        return resume;
    }

    @Transactional
    public Resume updateResume(ResumeRequest resumeRequest, long resumeId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        if (resume.getUser().getId() != userId) {
            throw new CustomException("You do not have permission to update a resume.", ErrorCode.ACCESS_DENIED);
        }

        Resume updatedResume = ResumeMapper.INSTANCE.resumeRequestToResume(resumeRequest);
        resume.updateResume(updatedResume);
        resumeRepository.save(resume);

        return resume;
    }

    @Transactional
    public void deleteResume(long resumeId, long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        if (resume.getUser().getId() != userId) {
            throw new CustomException("You do not have permission to delete a resume.", ErrorCode.ACCESS_DENIED);
        }
        resumeRepository.deleteById(resumeId);
    }

    public List<Resume> getAllResumeByUserId(long userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        return resumeRepository.findByUserId(userId);
    }

    public Resume getResume(long userId, long resumeId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));

        if (userId == 0 || userId != resume.getUser().getId()) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
        return resume;
    }

}
