package com.example.eumserver.domain.application.team.mapper;

import com.example.eumserver.domain.application.team.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.team.entity.TeamApplication;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationMapper {
    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);


    MyApplicationResponse entityToMyApplicationResponse(TeamApplication application);
}
