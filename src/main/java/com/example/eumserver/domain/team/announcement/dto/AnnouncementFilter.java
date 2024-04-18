package com.example.eumserver.domain.team.announcement.dto;

import com.example.eumserver.domain.team.announcement.domain.OccupationClassification;

import java.util.List;

public record AnnouncementFilter(
        boolean published,
        List<OccupationClassification> occupationClassifications
) {
}
