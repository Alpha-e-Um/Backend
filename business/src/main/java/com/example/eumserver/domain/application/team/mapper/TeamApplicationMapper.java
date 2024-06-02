package com.example.eumserver.domain.application.team.mapper;

import com.example.eumserver.domain.application.team.dto.MyTeamApplicationResponse;
import com.example.eumserver.domain.application.team.entity.TeamApplication;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamApplicationMapper {
    TeamApplicationMapper INSTANCE = Mappers.getMapper(TeamApplicationMapper.class);


    @Mapping(target = "createDate", source = "timeStamp.createDate")
    MyTeamApplicationResponse entityToMyApplicationResponse(TeamApplication application);
}
