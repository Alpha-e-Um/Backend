package com.example.eumserver.domain.announcement.team.mapper;

import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementResponse;
import com.example.eumserver.domain.announcement.team.domain.TeamAnnouncement;
import com.example.eumserver.domain.announcement.team.dto.TeamAnnouncementRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamAnnouncementMapper {

    TeamAnnouncementMapper INSTANCE = Mappers.getMapper(TeamAnnouncementMapper.class);

    @Mapping(target = "createDate", source = "timeStamp.createDate")
    @Mapping(target = "teamId", source = "team.id")
    @Mapping(target = "teamLogo", source = "team.logo")
    TeamAnnouncementResponse entityToResponse(TeamAnnouncement announcement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    @Mapping(target = "publishedDate", ignore = true)
    @Mapping(target = "team", ignore = true)
    @Mapping(target = "applications", ignore = true)
    TeamAnnouncement requestToEntity(TeamAnnouncementRequest announcementRequest);
}
