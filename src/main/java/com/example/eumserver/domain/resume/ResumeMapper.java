package com.example.eumserver.domain.resume;

import com.example.eumserver.domain.resume.dto.ResumeRequest;
import com.example.eumserver.domain.resume.entity.Resume;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResumeMapper {
    ResumeMapper INSTANCE = Mappers.getMapper(ResumeMapper.class);

    @Mapping(source = "jobCategory", target = "jobCategory")
    @Mapping(source = "jobSubcategory", target = "jobSubcategory")
    @Mapping(target = "user", ignore = true)
    @Mapping(source = "gpa", target = "gpa")
    @Mapping(source = "totalScore", target = "totalScore")
    @Mapping(source = "introduction", target = "introduction")
    @Mapping(source = "careers", target = "careers")
    @Mapping(source = "certificates", target = "certificates")
    @Mapping(source = "projects", target = "projects")
    Resume resumeRequestToResume(ResumeRequest resumeRequest);
}
