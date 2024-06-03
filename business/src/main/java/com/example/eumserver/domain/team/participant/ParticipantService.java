package com.example.eumserver.domain.team.participant;

import com.example.eumserver.domain.user.UserMapper;
import com.example.eumserver.domain.user.domain.User;
import com.example.eumserver.domain.user.dto.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParticipantService {

    private final ParticipantRepository participantRepository;

    public List<UserResponse> getAllParticipantByTeamId(long teamId) {
        List<Participant> participants = participantRepository.findAllByTeamId(teamId);
        return participants.stream().map(participant -> {
            User user = participant.getUser();
            return UserMapper.INSTANCE.userToUserResponse(user);
        }).toList();
    }

}
