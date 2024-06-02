package com.example.eumserver.domain.application.service;

import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.repository.TeamAnnouncementRepository;
import com.example.eumserver.domain.application.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.entity.ApplicationState;
import com.example.eumserver.domain.application.entity.TeamApplication;
import com.example.eumserver.domain.application.repository.ApplicationRepository;
import com.example.eumserver.domain.resume.ResumeRepository;
import com.example.eumserver.domain.resume.entity.Resume;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.team.participant.ParticipantRole;
import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ApplicationService {

    final private ApplicationRepository applicationRepository;
    final private TeamAnnouncementRepository teamAnnouncementRepository;
    final private UserRepository userRepository;
    final private ResumeRepository resumeRepository;

    public Page<MyApplicationResponse> getMyApplications(Long user_id, ApplicationState state, Integer page) {
        Pageable paging = PageRequest.of(page, 10);
        return applicationRepository
                .getMyApplicationsWithPaging(user_id, state, paging);
    }

    public void checkApply(long userId, long announcementId) {
        if (applicationRepository.checkApplicationExist(userId, announcementId)) {
            throw new CustomException(ErrorCode.ALREADY_APPLIED_ANNOUNCEMENT);
        }
    }

    @Transactional
    public TeamApplication applyTeam(long userId, long announcementId, Long resumeId) {
        checkApply(userId, announcementId);

        TeamAnnouncement announcement = teamAnnouncementRepository.findById(announcementId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_ANNOUNCEMENT_NOT_FOUND));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Resume resume = null;
        if (resumeId != null) {
            resume = resumeRepository.findById(resumeId)
                    .orElseThrow(() -> new CustomException(ErrorCode.RESUME_NOT_FOUND));
        }

        if (announcement.getExpiredDate().isBefore(LocalDateTime.now()) ||
                announcement.getVacancies() < 1)
            throw new CustomException(ErrorCode.EXPIRED_ANNOUNCEMENT);

        TeamApplication application = TeamApplication.builder()
                .announcement(announcement)
                .user(user)
                .resume(resume)
                .state(ApplicationState.PENDING)
                .build();

        applicationRepository.save(application);

        return application;
    }

    @Transactional
    public TeamApplication cancelApplication(Long userId, Long applicationId) {
        TeamApplication application = applicationRepository.getApplicationWithState(userId, applicationId, ApplicationState.PENDING);

        if (application == null) throw new CustomException(ErrorCode.TEAM_APPLICATION_CANT_CANCEL);

        application.cancel();
        applicationRepository.save(application);

        return application;
    }

    public TeamApplication acceptApplication(Long userId, Long applicationId) {
        TeamApplication teamApplication = getTeamApplication(applicationId);

        // 해당 유저의 권한 확인
        checkAuthority(teamApplication, userId);

        // 현재 지원 상태 확인
        checkState(teamApplication);

        // 지원 상태 업데이트
        teamApplication.accept();

        // TODO: 합격 결과 전송
        return applicationRepository.save(teamApplication);
    }

    public TeamApplication rejectApplication(Long userId, Long applicationId) {
        TeamApplication teamApplication = getTeamApplication(applicationId);

        checkAuthority(teamApplication, userId);

        // 현재 지원 상태 확인
        checkState(teamApplication);

        teamApplication.reject();

        // TODO: 불합격 결과 전송
        return applicationRepository.save(teamApplication);
    }

    private TeamApplication getTeamApplication(Long applicationId) {
        return applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_APPLICATION_NOT_FOUND));
    }

    private void checkAuthority(TeamApplication teamApplication, Long userId) {
        Optional<Participant> _participant = teamApplication
                .getAnnouncement()
                .getTeam()
                .getParticipants().stream()
                .filter(participant -> Objects.equals(participant.getUser().getId(), userId)).findFirst();

        if (_participant.isEmpty() || _participant.get().getRole() == ParticipantRole.MEMBER) {
            throw new CustomException(ErrorCode.ACCESS_DENIED);
        }
    }

    private void checkState(TeamApplication teamApplication) {
        if (teamApplication.getState() != ApplicationState.PENDING) {
            throw new CustomException(ErrorCode.TEAM_APPLICATION_CANT_REJECTED);
        }
    }
}
