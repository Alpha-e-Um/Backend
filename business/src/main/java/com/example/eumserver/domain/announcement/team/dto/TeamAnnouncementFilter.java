package com.example.eumserver.domain.announcement.team.dto;

import com.example.eumserver.domain.announcement.filter.domain.OccupationClassification;
import lombok.Data;

import java.util.List;

@Data
public class TeamAnnouncementFilter{
    private final int page;
    private final int size;
    private final List<OccupationClassification> occupationClassifications;

    public TeamAnnouncementFilter(int page, int size, List<String> occupationClassifications) {
        this.page = page;
        this.size = size;
        this.occupationClassifications = occupationClassifications.stream().map(
                OccupationClassification::from
        ).toList();
    }
}
