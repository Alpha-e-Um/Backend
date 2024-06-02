package com.example.eumserver.domain.team;

import com.example.eumserver.domain.team.dto.TeamRequest;
import com.example.eumserver.domain.team.dto.TeamResponse;
import com.example.eumserver.domain.team.participant.ParticipantRole;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "formationDate", ignore = true)
    @Mapping(target = "announcements", ignore = true)
    Team teamRequestToTeam(TeamRequest teamRequest);

    @Mapping(target = "isOwner", source = "team", qualifiedByName = "mapIsOwner")
    TeamResponse teamToTeamResponse(Team team, @Context long userId);

    @Named("mapIsOwner")
    default Boolean mapIsOwner(Team team, @Context long userId) {
        return team.getParticipants().stream().anyMatch(participant ->
                participant.getUser().getId() == userId && participant.getRole() == ParticipantRole.OWNER);
    }

}
