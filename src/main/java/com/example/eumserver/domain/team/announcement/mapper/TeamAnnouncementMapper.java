package com.example.eumserver.domain.team.announcement.mapper;

import com.example.eumserver.domain.team.announcement.domain.TeamAnnouncement;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.TeamAnnouncementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TeamAnnouncementMapper {

    TeamAnnouncementMapper INSTANCE = Mappers.getMapper(TeamAnnouncementMapper.class);

    TeamAnnouncementResponse entityToResponse(TeamAnnouncement announcement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    @Mapping(target = "publishedDate", ignore = true)
    @Mapping(target = "team", ignore = true)
    TeamAnnouncement requestToEntity(TeamAnnouncementRequest announcementRequest);
}
