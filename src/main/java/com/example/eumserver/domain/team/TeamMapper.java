package com.example.eumserver.domain.team;

import com.example.eumserver.domain.team.dto.TeamRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamMapper {

    TeamMapper INSTANCE = Mappers.getMapper(TeamMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "logo", ignore = true)
    @Mapping(target = "participants", ignore = true)
    @Mapping(target = "formationDate", ignore = true)
    Team teamRequestToTeam(TeamRequest teamRequest);

}
