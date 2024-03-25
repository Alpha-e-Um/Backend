package com.example.eumserver.domain.team;

import com.example.eumserver.domain.team.dto.TeamRequest;
import com.example.eumserver.domain.team.participant.Participant;
import com.example.eumserver.domain.team.participant.ParticipantId;
import com.example.eumserver.domain.team.participant.ParticipantRepository;
import com.example.eumserver.domain.user.User;
import com.example.eumserver.domain.user.UserRepository;
import com.example.eumserver.global.error.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;
    private final ParticipantRepository participantRepository;

    @Transactional
    public void createTeam(
            String email,
            TeamRequest teamRequest) {
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new EntityNotFoundException("User not found."));

        Team team = TeamMapper.INSTANCE.teamRequestToTeam(teamRequest);
        teamRepository.save(team);
        Participant participant = Participant.builder()
                .user(user)
                .team(team)
                .build();
        participantRepository.save(participant);
    }

    @Transactional
    public void deleteTeam(long userId, long teamId) {
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
