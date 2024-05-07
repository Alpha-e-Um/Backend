package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.team.domain.OccupationClassification;

import java.util.List;

public record TeamAnnouncementFilter(
        boolean published,
        List<OccupationClassification> occupationClassifications
) {
}
