package com.example.eumserver.domain.team;

import com.example.eumserver.domain.team.dto.TeamRequest;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.team.participant.ParticipantId;
import com.example.eumserver.domain.team.participant.ParticipantRepository;
import com.example.eumserver.domain.team.participant.ParticipantRole;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.exception.CustomException;
import com.example.eumserver.global.error.exception.ErrorCode;
import com.example.eumserver.global.utils.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;
    private final S3Uploader s3Uploader;

    @Transactional
    public Team createTeam(
            long userId,
            TeamRequest teamRequest,
            MultipartFile logo) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Team team = TeamMapper.INSTANCE.teamRequestToTeam(teamRequest);
        String uploadUrl = s3Uploader.uploadToS3(S3Uploader.S3Path.TEAM_IMAGE, logo);
        team.setLogo(uploadUrl);
        teamRepository.save(team);

        Participant participant = Participant.builder()
                .user(user)
                .team(team)
                .role(ParticipantRole.OWNER)
                .build();
        participantRepository.save(participant);

        return team;
    }

    @Transactional
    public void deleteTeam(long userId, long teamId) {
        Optional<Participant> _participant = participantRepository.findById(new ParticipantId(userId, teamId));
        if (_participant.isEmpty() || _participant.get().getRole() != ParticipantRole.OWNER) {
            throw new CustomException("You do not have permission to delete a team.", ErrorCode.ACCESS_DENIED);
        }
        participantRepository.deleteById(new ParticipantId(userId, teamId));
    }


    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.TEAM_NOT_FOUND));
    }

}
