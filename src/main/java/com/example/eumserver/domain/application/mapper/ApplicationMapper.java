package com.example.eumserver.domain.application.mapper;

import com.example.eumserver.domain.application.dto.MyApplicationResponse;
import com.example.eumserver.domain.application.entity.Application;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ApplicationMapper {
    ApplicationMapper INSTANCE = Mappers.getMapper(ApplicationMapper.class);


    MyApplicationResponse entityToMyApplicationResponse(Application application);
}
