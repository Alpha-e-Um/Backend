package com.example.eumserver.domain.announcement.resume.mapper;

import com.example.eumserver.domain.announcement.resume.domain.ResumeAnnouncement;
import com.example.eumserver.domain.announcement.resume.dto.ResumeAnnouncementRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ResumeAnnouncementMapper {

    ResumeAnnouncementMapper INSTANCE = Mappers.getMapper(ResumeAnnouncementMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "resume", ignore = true)
    ResumeAnnouncement requestToEntity(ResumeAnnouncementRequest resumeAnnouncementRequest);
}
