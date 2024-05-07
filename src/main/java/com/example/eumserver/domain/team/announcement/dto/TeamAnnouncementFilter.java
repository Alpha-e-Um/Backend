package com.example.eumserver.domain.team.announcement.dto;

import com.example.eumserver.domain.team.announcement.domain.OccupationClassification;

import java.util.List;

public record TeamAnnouncementFilter(
        boolean published,
        List<OccupationClassification> occupationClassifications
) {
}