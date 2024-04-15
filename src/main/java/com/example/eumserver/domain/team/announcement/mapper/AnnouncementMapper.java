package com.example.eumserver.domain.team.announcement.mapper;

import com.example.eumserver.domain.team.announcement.domain.Announcement;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementRequest;
import com.example.eumserver.domain.team.announcement.dto.AnnouncementResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AnnouncementMapper {

    AnnouncementMapper INSTANCE = Mappers.getMapper(AnnouncementMapper.class);

    AnnouncementResponse entityToResponse(Announcement announcement);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "timeStamp", ignore = true)
    @Mapping(target = "publishedDate", ignore = true)
    @Mapping(target = "team", ignore = true)
    Announcement requestToEntity(AnnouncementRequest announcementRequest);
}
