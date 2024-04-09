package com.example.eumserver.domain.team;

import com.example.eumserver.domain.team.dto.TeamRequest;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.team.participant.ParticipantId;
import com.example.eumserver.domain.team.participant.ParticipantRepository;
import com.example.eumserver.domain.team.participant.ParticipantRole;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.CustomException;
import com.example.eumserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public Team createTeam(
            long userId,
            TeamRequest teamRequest) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Team team = TeamMapper.INSTANCE.teamRequestToTeam(teamRequest);
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
            throw new CustomException(403, "You do not have permission to delete a team.");
        }
        participantRepository.deleteById(new ParticipantId(userId, teamId));
    }


    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team findById(Long id) {
        return teamRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Team not found."));
    }

}
